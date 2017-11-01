package me.okmanideep.androidgarage.ui.scrolling

import android.os.Bundle
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.CheckBox
import com.jakewharton.rxbinding.widget.RxCompoundButton
import kotterknife.bindView
import me.okmanideep.androidgarage.R
import rx.Observable
import rx.subscriptions.CompositeSubscription
import timber.log.Timber

class ScrollingDemo : AppCompatActivity() {
    val NORMAL = 0
    val BETTER = 1
    val FEED_ROOT = 2

    val rvNormal : RecyclerView by bindView(R.id.rv_normal)
    val rvBetter : RecyclerView by bindView(R.id.rv_better)
    val rvFeedRoot : RecyclerView by bindView(R.id.rv_feed_root)
    val cbAngle : CheckBox by bindView(R.id.cb_consider_angle)
    val cbIgore : CheckBox by bindView(R.id.cb_ignore_child_requests)

    val adapter = SectionAdapter()
    lateinit var viewSubscriptions : CompositeSubscription

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scrolling_demo)
        setUpDrawer()

        rvNormal.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvBetter.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvFeedRoot.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        rvNormal.adapter = adapter
        rvBetter.adapter = adapter
        rvFeedRoot.adapter = adapter
    }

    private fun setUpDrawer() {
        val toolbar:Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)


        val drawerLayout:DrawerLayout = findViewById(R.id.drawer_layout)
        val drawerToggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.consider_angle, R.string.ignore_child_requests)
        drawerToggle.syncState()
    }

    override fun onStart() {
        super.onStart()
        viewSubscriptions = CompositeSubscription()

        setup()
    }

    override fun onStop() {
        super.onStop()

        unSubscribe()
    }

    /** PRESENTER :P **/
    fun setup() {
        val angleChanges = angleChecks().share()

        viewSubscriptions.add(
                Observable.combineLatest(angleChanges, ignoreChecks(), { a, i -> setting(a,i)})
                    .subscribe({ s -> applySetting(s) }, {e -> Timber.e(e, "combined changes")})
        )

        viewSubscriptions.add(
                angleChanges.subscribe({a -> enableIgnore(a)}, {e -> Timber.e(e, "angle Changes")})
        )
    }

    fun unSubscribe() {
        if(!viewSubscriptions.isUnsubscribed) {
            viewSubscriptions.unsubscribe()
        }
    }

    private fun setting(angle : Boolean, ignore : Boolean) : Int {
        if(!angle) {
            return NORMAL
        } else {
            return if (ignore) FEED_ROOT else BETTER
        }
    }
    /** END PRESENTER **/


    /** VIEW **/
    fun angleChecks() : Observable<Boolean> {
        return RxCompoundButton.checkedChanges(cbAngle)
    }

    fun ignoreChecks() : Observable<Boolean> {
        return RxCompoundButton.checkedChanges(cbIgore)
    }

    fun enableIgnore(enable : Boolean) {
        cbIgore.isEnabled = enable
    }

    fun applySetting(setting : Int) {
        rvNormal.visibility = if(setting == NORMAL) View.VISIBLE else View.GONE
        rvBetter.visibility = if(setting == BETTER) View.VISIBLE else View.GONE
        rvFeedRoot.visibility = if(setting == FEED_ROOT) View.VISIBLE else View.GONE
    }
    /** END VIEW **/
}

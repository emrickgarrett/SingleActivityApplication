package emrickgj.saa;

import android.app.ActivityManager;
import android.content.ComponentCallbacks2;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import java.util.LinkedList;

public abstract class SingleActivity extends AppCompatActivity implements ComponentCallbacks2 {

    public static String SAA = "SAA";

    LinkedList<Class> history;

    private ViewGroup container;
    private ViewController activeViewController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        history = new LinkedList<Class>();
    }

    public void setContentView(int layout_id, int container_id) {
        setContentView(layout_id);
        container = findViewById(container_id);
    }

    public void navigateToViewController(ViewController vc) {
        container.removeAllViews();
        container.addView(vc.getView());
        history.addFirst(vc.getClass());

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.weight = 1;
        vc.getView().setLayoutParams(layoutParams);

        activeViewController = vc;
        activeViewController.onViewControllerLoaded();
    }

    public void navigateToViewController(final ViewController vc, int transition_out, int transition_in) {
        Animation animIn = AnimationUtils.loadAnimation(this, transition_out);
        Animation animOut = AnimationUtils.loadAnimation(this, transition_in);
        animOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                navigateToViewController(vc);
            }

            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
        activeViewController.getView().startAnimation(animIn);
    }

    public void popNavigateToViewController(ViewController vc) {
        history.removeFirst();
        navigateToViewController(vc);
    }

    public void popNavigateToViewController(ViewController vc, int transition_out, int transition_in) {
        history.removeFirst();
        navigateToViewController(vc, transition_out, transition_in);
    }

    public void popViewController() {
        activeViewController.onViewControllerDestroyed();
        history.removeFirst();
        
        try {
            this.navigateToViewController(
                    (ViewController) history.getFirst()
                            .asSubclass(ViewController.class)
                            .getDeclaredConstructor(SingleActivity.class)
                            .newInstance(this)
            );
        } catch(InstantiationException ex) {
            Log.e(SAA, "Error Instantiation ViewController Object on Pop", ex);
        } catch(IllegalAccessException ex) {
            Log.e(SAA, "Error Accessing ViewController Class on Pop", ex);
        } catch(ClassCastException ex) {
            Log.e(SAA, "Error Casting Object to ViewController on Pop", ex);
        } catch(Exception ex) {
            Log.e(SAA, "General Exception in ViewController on Pop", ex);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        activeViewController.onViewControllerDestroyed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        activeViewController.onViewControllerResumed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        activeViewController.onViewControllerPaused();
    }

    @Override
    protected void onStop() {
        super.onStop();
        activeViewController.onViewControllerStopped();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        activeViewController.onViewControllerRestarted();
    }

    @Override
    public void onBackPressed() {
        if(history.size() == 1) {
            super.onBackPressed();
        } else {
            popViewController();
        }
    }

    public void onTrimMemory(int level) {
        activeViewController.onTrimMemory(level);
    }

    protected ActivityManager.MemoryInfo getMemoryInformation() {
        ActivityManager activityManager = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);
        return memoryInfo;
    }

    protected ViewController getActiveViewController() {
        return this.activeViewController;
    }

    protected ViewGroup getContainer() {
        return this.container;
    }
}

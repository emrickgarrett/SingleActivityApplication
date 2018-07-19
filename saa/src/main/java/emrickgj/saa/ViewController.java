package emrickgj.saa;

import android.view.View;

public abstract class ViewController {

    protected View view;
    protected SingleActivity context;

    public ViewController(SingleActivity context) {
        this.context = context;

        inflateView();
    }

    protected abstract void inflateView();

    public void onViewControllerDestroyed() {}
    public void onViewControllerLoaded() {}
    public void onViewControllerPaused() {}
    public void onViewControllerResumed() {}
    public void onViewControllerStopped() {}
    public void onViewControllerRestarted() {}
    public void onTrimMemory(int level) {}

    public View getView() { return this.view; }

    public void popViewController() {
        context.popViewController();
    }

    public void navigate(ViewController vc) {
        context.navigateToViewController(vc);
    }

    public void navigate(ViewController vc, int transition_out, int transition_in) {
        context.navigateToViewController(vc, transition_out, transition_in);
    }

    public void popNavigate(ViewController vc) {
        context.popNavigateToViewController(vc);
    }

    public void popNavigateViewController(ViewController vc, int transition_out, int transition_in) {
        context.popNavigateToViewController(vc, transition_out, transition_in);
    }
}
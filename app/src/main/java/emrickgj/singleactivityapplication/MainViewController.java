package emrickgj.singleactivityapplication;

import emrickgj.saa.SingleActivity;
import emrickgj.saa.ViewController;

public class MainViewController extends ViewController {

    public MainViewController(SingleActivity context) {
        super(context);
    }
    @Override
    protected void inflateView() {
        this.view = context.getLayoutInflater().inflate(R.layout.view_main, null);
    }
}

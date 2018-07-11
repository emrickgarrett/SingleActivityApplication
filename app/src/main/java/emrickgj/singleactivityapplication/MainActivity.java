package emrickgj.singleactivityapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class MainActivity extends emrickgj.saa.SingleActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main, R.id.main_linear_layout);

        MainViewController mainVc = new MainViewController(this);
        this.navigateToViewController(mainVc);
    }
}

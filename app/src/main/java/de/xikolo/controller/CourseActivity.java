package de.xikolo.controller;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import de.xikolo.R;
import de.xikolo.controller.exceptions.WrongParameterException;
import de.xikolo.controller.course.CourseFragment;
import de.xikolo.model.Course;

public class CourseActivity extends FragmentActivity {

    public static final String TAG = CourseActivity.class.getSimpleName();

    public static final String ARG_COURSE = "arg_course";

    private Course mCourse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);

        Bundle b = getIntent().getExtras();
        if (b == null || !b.containsKey(ARG_COURSE)) {
            throw new WrongParameterException();
        } else {
            this.mCourse = b.getParcelable(ARG_COURSE);
        }

        setTitle(mCourse.name);

        String tag = "content";

        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.findFragmentByTag(tag) == null) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.container, CourseFragment.newInstance(mCourse), tag);
            transaction.commit();
        }
    }

}
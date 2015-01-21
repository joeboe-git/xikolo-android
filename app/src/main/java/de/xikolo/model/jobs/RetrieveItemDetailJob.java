package de.xikolo.model.jobs;

import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;

import java.lang.reflect.Type;
import java.util.concurrent.atomic.AtomicInteger;

import de.xikolo.GlobalApplication;
import de.xikolo.data.entities.Course;
import de.xikolo.data.entities.Module;
import de.xikolo.data.net.JsonRequest;
import de.xikolo.data.entities.Item;
import de.xikolo.data.entities.ItemAssignment;
import de.xikolo.data.entities.ItemText;
import de.xikolo.data.entities.ItemVideo;
import de.xikolo.model.Result;
import de.xikolo.model.UserModel;
import de.xikolo.util.Config;
import de.xikolo.util.NetworkUtil;

public class RetrieveItemDetailJob extends Job {

    public static final String TAG = RetrieveItemDetailJob.class.getSimpleName();

    private static final AtomicInteger jobCounter = new AtomicInteger(0);

    private final int id;

    private Result<Item> result;
    private Course course;
    private Module module;
    private Item item;
    private String itemType;

    public RetrieveItemDetailJob(Result<Item> result, Course course, Module module, Item item, String itemType) {
        super(new Params(Priority.HIGH));
        id = jobCounter.incrementAndGet();

        this.result = result;
        this.course = course;
        this.module = module;
        this.item = item;
        this.itemType = itemType;
    }

    @Override
    public void onAdded() {
        if (Config.DEBUG) Log.i(TAG, TAG + " added | course.id " + course.id + " | module.id " + module.id + " | item.id " + item.id + " | itemType " + itemType);
    }

    @Override
    public void onRun() throws Throwable {
        if (!UserModel.isLoggedIn(GlobalApplication.getInstance()) || !course.is_enrolled) {
            result.error(Result.ErrorCode.NO_AUTH);
        } else if (!NetworkUtil.isOnline(GlobalApplication.getInstance())) {
            result.error(Result.ErrorCode.NO_NETWORK);
        } else {
            Type type = null;
            if (itemType.equals(Item.TYPE_TEXT)) {
                type = new TypeToken<Item<ItemText>>(){}.getType();
            } else if (itemType.equals(Item.TYPE_VIDEO)) {
                type = new TypeToken<Item<ItemVideo>>(){}.getType();
            } else if (itemType.equals(Item.TYPE_SELFTEST)
                    || itemType.equals(Item.TYPE_ASSIGNMENT)
                    || itemType.equals(Item.TYPE_EXAM)) {
                type = new TypeToken<Item<ItemAssignment>>(){}.getType();
            }

            String url = Config.API + Config.COURSES + course.id + "/"
                    + Config.MODULES + module.id + "/" + Config.ITEMS + item.id;

            JsonRequest request = new JsonRequest(url, type);
            request.setToken(UserModel.getToken(GlobalApplication.getInstance()));

            Object o = request.getResponse();
            if (o != null) {
                Item item = (Item) o;
                if (Config.DEBUG) Log.i(TAG, "ItemDetail received");
                result.success(item, Result.DataSource.NETWORK);
            } else {
                if (Config.DEBUG) Log.w(TAG, "No ItemDetail received");
                result.error(Result.ErrorCode.NO_RESULT);
            }
        }
    }

    @Override
    protected void onCancel() {
        result.error(Result.ErrorCode.ERROR);
    }

    @Override
    protected boolean shouldReRunOnThrowable(Throwable throwable) {
        return false;
    }

}

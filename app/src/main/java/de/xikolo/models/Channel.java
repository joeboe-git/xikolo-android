package de.xikolo.models;

import android.graphics.Color;
import android.os.Build;
import android.util.Log;

import com.squareup.moshi.Json;

import de.xikolo.App;
import de.xikolo.R;
import de.xikolo.config.Config;
import de.xikolo.models.base.RealmAdapter;
import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import moe.banana.jsonapi2.JsonApi;
import moe.banana.jsonapi2.Resource;

public class Channel extends RealmObject {

    public static final String TAG = Channel.class.getSimpleName();

    @PrimaryKey
    public String id;

    public String title;

    public String slug;

    public String color;

    public int position;

    public String description;

    public String imageUrl;

    public static Channel get(String id) {
        Realm realm = Realm.getDefaultInstance();
        Channel model = realm.where(Channel.class).equalTo("id", id).findFirst();
        if (model != null) model = realm.copyFromRealm(model);
        realm.close();
        return model;
    }

    public int getColorOrDefault() {
        if (color != null)
            try {
                return Color.parseColor(color);
            } catch (IllegalArgumentException e) {
                if (Config.DEBUG) Log.d(TAG, "Channel color '" + color + "' could not be parsed");
            }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            return App.getInstance().getResources().getColor(R.color.apptheme_main, null);
        else
            return App.getInstance().getResources().getColor(R.color.apptheme_main);
    }

    @JsonApi(type = "channels")
    public static class JsonModel extends Resource implements RealmAdapter<Channel> {

        public String title;

        public String slug;

        public String color;

        public int position;

        public String description;

        @Json(name = "mobile_image_url")
        public String mobileImageUrl;

        @Override
        public Channel convertToRealmObject() {
            Channel model = new Channel();
            model.id = getId();
            model.title = title;
            model.slug = slug;
            model.color = color;
            model.position = position;
            model.description = description;
            model.imageUrl = mobileImageUrl;

            return model;
        }

    }

}
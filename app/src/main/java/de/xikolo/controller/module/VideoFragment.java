package de.xikolo.controller.module;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import de.xikolo.R;
import de.xikolo.controller.VideoActivity;
import de.xikolo.controller.helper.VideoController;
import de.xikolo.data.entities.Course;
import de.xikolo.data.entities.Item;
import de.xikolo.data.entities.Module;
import de.xikolo.data.entities.VideoItemDetail;
import de.xikolo.model.DownloadModel;
import de.xikolo.model.ItemModel;
import de.xikolo.model.Result;
import de.xikolo.util.NetworkUtil;
import de.xikolo.util.ToastUtil;
import de.xikolo.view.IconButton;

public class VideoFragment extends PagerFragment<VideoItemDetail> {

    public static final String TAG = VideoFragment.class.getSimpleName();

    public static final int FULL_SCREEN_REQUEST = 1;

    public static final String KEY_ITEM = "key_item";

    private TextView mTitle;
    private View mContainer;
    private ProgressBar mProgress;

    private TextView mTextDownloadVideoHd;
    private IconButton mButtonDownloadVideoHd;
    private TextView mTextDownloadVideoSd;
    private IconButton mButtonDownloadVideoSd;
    private TextView mTextDownloadSlides;
    private IconButton mButtonDownloadSlides;
    private TextView mTextDownloadTranscript;
    private IconButton mButtonDownloadTranscript;

    private ViewGroup mVideoContainer;
    private ViewGroup mVideoMetadata;

    private VideoController mVideoController;

    private ItemModel mItemModel;
    private Result<Item> mItemResult;

    private boolean wasSaved = false;

    public VideoFragment() {

    }

    public static PagerFragment newInstance(Course course, Module module, Item item) {
        return PagerFragment.newInstance(new VideoFragment(), course, module, item);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mItemModel = new ItemModel(getActivity(), jobManager, databaseHelper);
        mItemResult = new Result<Item>() {
            @Override
            protected void onSuccess(Item result, DataSource dataSource) {
                mItem = result;
                setupVideo();
            }

            @Override
            protected void onError(ErrorCode errorCode) {
                if (errorCode == ErrorCode.NO_NETWORK) {
                    NetworkUtil.showNoConnectionToast(getActivity());
                } else {
                    ToastUtil.show(getActivity(), R.string.error);
                }
                mProgress.setVisibility(View.VISIBLE);
                mContainer.setVisibility(View.GONE);
            }
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_video, container, false);

        mContainer = layout.findViewById(R.id.container);
        mProgress = (ProgressBar) layout.findViewById(R.id.progress);

        mTitle = (TextView) layout.findViewById(R.id.textTitle);

        final DownloadModel dm = new DownloadModel();
        
        mTextDownloadVideoHd = (TextView) layout.findViewById(R.id.textDownloadVideoHd);
        mButtonDownloadVideoHd = (IconButton) layout.findViewById(R.id.buttonDownloadVideoHd);
        mButtonDownloadVideoHd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dm.startDownload(mItem.detail.stream.hd_url, DownloadModel.DownloadFileType.VIDEO_HD, mCourse, mModule, mItem);
            }
        });
        mTextDownloadVideoSd = (TextView) layout.findViewById(R.id.textDownloadVideoSd);
        mButtonDownloadVideoSd = (IconButton) layout.findViewById(R.id.buttonDownloadVideoSd);
        mButtonDownloadVideoSd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dm.startDownload(mItem.detail.stream.sd_url, DownloadModel.DownloadFileType.VIDEO_SD, mCourse, mModule, mItem);
            }
        });
        mTextDownloadSlides = (TextView) layout.findViewById(R.id.textDownloadSlides);
        mButtonDownloadSlides = (IconButton) layout.findViewById(R.id.buttonDownloadSlides);
        mButtonDownloadSlides.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dm.startDownload(mItem.detail.slides_url, DownloadModel.DownloadFileType.SLIDES, mCourse, mModule, mItem);
            }
        });
        mTextDownloadTranscript = (TextView) layout.findViewById(R.id.textDownloadTranscript);
        mButtonDownloadTranscript = (IconButton) layout.findViewById(R.id.buttonDownloadTranscript);
        mButtonDownloadTranscript.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dm.startDownload(mItem.detail.transcript_url, DownloadModel.DownloadFileType.TRANSCRIPT, mCourse, mModule, mItem);
            }
        });
        
        mVideoContainer = (ViewGroup) layout.findViewById(R.id.videoContainer);
        mVideoMetadata = (ViewGroup) layout.findViewById(R.id.videoMetadata);

        mContainer.setVisibility(View.GONE);

        mVideoController = new VideoController(getActivity(), mVideoContainer);

        if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            Display display = getActivity().getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            mVideoController.setDimensions(size.x, size.x / 16 * 9);
        } else {
            Display display = getActivity().getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            mVideoController.setDimensions((int) (size.x * 0.6), (int) (size.x * 0.6 / 16 * 9));
            ViewGroup.LayoutParams params = mVideoContainer.getLayoutParams();
            params.width = (int) (size.x * 0.6);
            mVideoContainer.setLayoutParams(params);

            ViewGroup.LayoutParams params_meta = mVideoMetadata.getLayoutParams();
            params_meta.width = (int) (size.x * 0.6);
            mVideoMetadata.setLayoutParams(params_meta);
        }

        mVideoController.setOnFullscreenButtonClickedListener(new VideoController.OnFullscreenClickListener() {
            @Override
            public void onFullscreenClick(int currentPosition, boolean isPlaying, boolean isVideoQualityInHD, boolean didUserChangeVideoQuality) {
                Intent intent = new Intent(getActivity(), VideoActivity.class);
                Bundle b = new Bundle();
                b.putParcelable(KEY_ITEM, mItem);
                b.putInt(VideoController.KEY_TIME, currentPosition);
                b.putBoolean(VideoController.KEY_ISPLAYING, isPlaying);
                b.putBoolean(VideoController.KEY_VIDEO_QUALITY, isVideoQualityInHD);
                b.putBoolean(VideoController.KEY_DID_USER_CHANGE_QUALITY, didUserChangeVideoQuality);
                intent.putExtras(b);
                startActivityForResult(intent, FULL_SCREEN_REQUEST);
            }
        });

        if (savedInstanceState != null) {
            wasSaved = true;
            mVideoController.returnFromSavedInstanceState(savedInstanceState);
            mItem = savedInstanceState.getParcelable(KEY_ITEM);
        } else {
            mContainer.setVisibility(View.GONE);
        }

        return layout;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == FULL_SCREEN_REQUEST && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                wasSaved = true;
                mVideoController.returnFromSavedInstanceState(data.getExtras());
                mItem = data.getExtras().getParcelable(KEY_ITEM);
            }
        }
    }

    private void setupVideo() {
        wasSaved = true;
        mProgress.setVisibility(View.GONE);
        mContainer.setVisibility(View.VISIBLE);

        mVideoController.setVideo(mItem);

        mTitle.setText(mItem.detail.title);

        if (mItem.detail.stream.hd_url == null) {
            mTextDownloadVideoHd.setVisibility(View.GONE);
            mButtonDownloadVideoHd.setVisibility(View.GONE);
        }
        if (mItem.detail.stream.sd_url == null) {
            mTextDownloadVideoSd.setVisibility(View.GONE);
            mButtonDownloadVideoSd.setVisibility(View.GONE);
        }
        if (mItem.detail.slides_url == null) {
            mTextDownloadSlides.setVisibility(View.GONE);
            mButtonDownloadSlides.setVisibility(View.GONE);
        }
        // seems buggy at backend
//        if (mItem.detail.transcript_url == null) {
            mTextDownloadTranscript.setVisibility(View.GONE);
            mButtonDownloadTranscript.setVisibility(View.GONE);
//        }
    }

    @Override
    public void onStart() {
        super.onStart();

        if (!wasSaved) {
            mItemModel.getItemDetail(mItemResult, mCourse, mModule, mItem, Item.TYPE_VIDEO);
        } else {
            setupVideo();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(KEY_ITEM, mItem);
        if (mVideoController != null) {
            mVideoController.onSaveInstanceState(outState);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        inflater.inflate(R.menu.refresh, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
//            case R.id.action_refresh:
//                onRefresh();
//                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void pageChanged() {
        if (mVideoController != null) {
            mVideoController.pause();
            mVideoController.show();
        }
    }

    @Override
    public void pageScrolling(int state) {
        if (mVideoController != null) {
            mVideoController.hide();
        }
    }

}

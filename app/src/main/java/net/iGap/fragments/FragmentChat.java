package net.iGap.fragments;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.ArrayRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ViewStubCompat;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.text.format.DateUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.lalongooo.videocompressor.video.MediaController;
import com.mikepenz.fastadapter.IItemAdapter;
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;
import com.mikepenz.fastadapter.items.AbstractItem;
import com.vanniktech.emoji.EmojiPopup;
import com.vanniktech.emoji.listeners.OnEmojiBackspaceClickListener;
import com.vanniktech.emoji.listeners.OnEmojiPopupDismissListener;
import com.vanniktech.emoji.listeners.OnEmojiPopupShownListener;
import com.vanniktech.emoji.listeners.OnSoftKeyboardCloseListener;
import com.vanniktech.emoji.listeners.OnSoftKeyboardOpenListener;
import io.fabric.sdk.android.services.concurrency.AsyncTask;
import io.fotoapparat.Fotoapparat;
import io.fotoapparat.view.CameraRenderer;
import io.fotoapparat.view.CameraView;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import io.realm.Sort;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import me.leolin.shortcutbadger.ShortcutBadger;
import net.iGap.Config;
import net.iGap.G;
import net.iGap.R;
import net.iGap.activities.ActivityCall;
import net.iGap.activities.ActivityCrop;
import net.iGap.activities.ActivityMain;
import net.iGap.activities.ActivityTrimVideo;
import net.iGap.adapter.AdapterBottomSheet;
import net.iGap.adapter.MessagesAdapter;
import net.iGap.adapter.items.chat.AbstractMessage;
import net.iGap.adapter.items.chat.AudioItem;
import net.iGap.adapter.items.chat.ContactItem;
import net.iGap.adapter.items.chat.FileItem;
import net.iGap.adapter.items.chat.GifItem;
import net.iGap.adapter.items.chat.GifWithTextItem;
import net.iGap.adapter.items.chat.ImageItem;
import net.iGap.adapter.items.chat.ImageWithTextItem;
import net.iGap.adapter.items.chat.LocationItem;
import net.iGap.adapter.items.chat.LogItem;
import net.iGap.adapter.items.chat.ProgressWaiting;
import net.iGap.adapter.items.chat.TextItem;
import net.iGap.adapter.items.chat.TimeItem;
import net.iGap.adapter.items.chat.UnreadMessage;
import net.iGap.adapter.items.chat.VideoItem;
import net.iGap.adapter.items.chat.VideoWithTextItem;
import net.iGap.adapter.items.chat.ViewMaker;
import net.iGap.adapter.items.chat.VoiceItem;
import net.iGap.helper.HelperAvatar;
import net.iGap.helper.HelperCalander;
import net.iGap.helper.HelperDownloadFile;
import net.iGap.helper.HelperFragment;
import net.iGap.helper.HelperGetAction;
import net.iGap.helper.HelperGetDataFromOtherApp;
import net.iGap.helper.HelperGetMessageState;
import net.iGap.helper.HelperLog;
import net.iGap.helper.HelperMimeType;
import net.iGap.helper.HelperNotificationAndBadge;
import net.iGap.helper.HelperPermision;
import net.iGap.helper.HelperSaveFile;
import net.iGap.helper.HelperSetAction;
import net.iGap.helper.HelperString;
import net.iGap.helper.HelperTimeOut;
import net.iGap.helper.HelperUploadFile;
import net.iGap.helper.HelperUrl;
import net.iGap.helper.ImageHelper;
import net.iGap.interfaces.FinishActivity;
import net.iGap.interfaces.ICallFinish;
import net.iGap.interfaces.IDispatchTochEvent;
import net.iGap.interfaces.IMessageItem;
import net.iGap.interfaces.IOnBackPressed;
import net.iGap.interfaces.IPickFile;
import net.iGap.interfaces.IResendMessage;
import net.iGap.interfaces.ISendPosition;
import net.iGap.interfaces.IUpdateLogItem;
import net.iGap.interfaces.OnActivityChatStart;
import net.iGap.interfaces.OnAvatarGet;
import net.iGap.interfaces.OnBackgroundChanged;
import net.iGap.interfaces.OnChannelAddMessageReaction;
import net.iGap.interfaces.OnChannelGetMessagesStats;
import net.iGap.interfaces.OnChatClearMessageResponse;
import net.iGap.interfaces.OnChatDelete;
import net.iGap.interfaces.OnChatDeleteMessageResponse;
import net.iGap.interfaces.OnChatEditMessageResponse;
import net.iGap.interfaces.OnChatMessageRemove;
import net.iGap.interfaces.OnChatMessageSelectionChanged;
import net.iGap.interfaces.OnChatSendMessage;
import net.iGap.interfaces.OnChatSendMessageResponse;
import net.iGap.interfaces.OnChatUpdateStatusResponse;
import net.iGap.interfaces.OnClearChatHistory;
import net.iGap.interfaces.OnClickCamera;
import net.iGap.interfaces.OnClientJoinByUsername;
import net.iGap.interfaces.OnComplete;
import net.iGap.interfaces.OnDeleteChatFinishActivity;
import net.iGap.interfaces.OnGetPermission;
import net.iGap.interfaces.OnGroupAvatarResponse;
import net.iGap.interfaces.OnHelperSetAction;
import net.iGap.interfaces.OnLastSeenUpdateTiming;
import net.iGap.interfaces.OnMessageReceive;
import net.iGap.interfaces.OnPathAdapterBottomSheet;
import net.iGap.interfaces.OnSetAction;
import net.iGap.interfaces.OnUpdateUserOrRoomInfo;
import net.iGap.interfaces.OnUpdateUserStatusInChangePage;
import net.iGap.interfaces.OnUserContactsBlock;
import net.iGap.interfaces.OnUserContactsUnBlock;
import net.iGap.interfaces.OnUserInfoResponse;
import net.iGap.interfaces.OnUserUpdateStatus;
import net.iGap.interfaces.OnVoiceRecord;
import net.iGap.libs.rippleeffect.RippleView;
import net.iGap.module.AndroidUtils;
import net.iGap.module.AppUtils;
import net.iGap.module.AttachFile;
import net.iGap.module.ChatSendMessageUtil;
import net.iGap.module.CircleImageView;
import net.iGap.module.ContactUtils;
import net.iGap.module.DialogAnimation;
import net.iGap.module.EmojiEditTextE;
import net.iGap.module.EmojiTextViewE;
import net.iGap.module.FileUploadStructure;
import net.iGap.module.FileUtils;
import net.iGap.module.IntentRequests;
import net.iGap.module.LastSeenTimeUtil;
import net.iGap.module.MaterialDesignTextView;
import net.iGap.module.MessageLoader;
import net.iGap.module.MusicPlayer;
import net.iGap.module.MyAppBarLayout;
import net.iGap.module.MyLinearLayoutManager;
import net.iGap.module.MyType;
import net.iGap.module.ResendMessage;
import net.iGap.module.SHP_SETTING;
import net.iGap.module.SUID;
import net.iGap.module.TimeUtils;
import net.iGap.module.VoiceRecord;
import net.iGap.module.enums.ChannelChatRole;
import net.iGap.module.enums.GroupChatRole;
import net.iGap.module.enums.LocalFileType;
import net.iGap.module.enums.ProgressState;
import net.iGap.module.enums.SendingStep;
import net.iGap.module.structs.StructBackGroundSeen;
import net.iGap.module.structs.StructBottomSheet;
import net.iGap.module.structs.StructChannelExtra;
import net.iGap.module.structs.StructCompress;
import net.iGap.module.structs.StructMessageAttachment;
import net.iGap.module.structs.StructMessageInfo;
import net.iGap.module.structs.StructUploadVideo;
import net.iGap.proto.ProtoChannelGetMessagesStats;
import net.iGap.proto.ProtoClientGetRoomHistory;
import net.iGap.proto.ProtoFileDownload;
import net.iGap.proto.ProtoGlobal;
import net.iGap.proto.ProtoResponse;
import net.iGap.realm.RealmAttachment;
import net.iGap.realm.RealmAttachmentFields;
import net.iGap.realm.RealmCallConfig;
import net.iGap.realm.RealmChannelExtra;
import net.iGap.realm.RealmChannelRoom;
import net.iGap.realm.RealmClientCondition;
import net.iGap.realm.RealmClientConditionFields;
import net.iGap.realm.RealmContacts;
import net.iGap.realm.RealmContactsFields;
import net.iGap.realm.RealmGroupRoom;
import net.iGap.realm.RealmMember;
import net.iGap.realm.RealmOfflineDelete;
import net.iGap.realm.RealmOfflineDeleteFields;
import net.iGap.realm.RealmOfflineEdited;
import net.iGap.realm.RealmOfflineSeen;
import net.iGap.realm.RealmRegisteredInfo;
import net.iGap.realm.RealmRegisteredInfoFields;
import net.iGap.realm.RealmRoom;
import net.iGap.realm.RealmRoomDraft;
import net.iGap.realm.RealmRoomFields;
import net.iGap.realm.RealmRoomMessage;
import net.iGap.realm.RealmRoomMessageContact;
import net.iGap.realm.RealmRoomMessageFields;
import net.iGap.realm.RealmRoomMessageLocation;
import net.iGap.realm.RealmUserInfo;
import net.iGap.request.RequestChannelDeleteMessage;
import net.iGap.request.RequestChannelEditMessage;
import net.iGap.request.RequestChannelUpdateDraft;
import net.iGap.request.RequestChatDelete;
import net.iGap.request.RequestChatDeleteMessage;
import net.iGap.request.RequestChatEditMessage;
import net.iGap.request.RequestChatUpdateDraft;
import net.iGap.request.RequestClientJoinByUsername;
import net.iGap.request.RequestClientSubscribeToRoom;
import net.iGap.request.RequestClientUnsubscribeFromRoom;
import net.iGap.request.RequestGroupDeleteMessage;
import net.iGap.request.RequestGroupEditMessage;
import net.iGap.request.RequestGroupUpdateDraft;
import net.iGap.request.RequestSignalingGetConfiguration;
import net.iGap.request.RequestUserContactsBlock;
import net.iGap.request.RequestUserContactsUnblock;
import net.iGap.request.RequestUserInfo;
import org.parceler.Parcels;

import static android.app.Activity.RESULT_CANCELED;
import static android.content.Context.CLIPBOARD_SERVICE;
import static android.content.Context.LOCATION_SERVICE;
import static android.content.Context.MODE_PRIVATE;
import static io.fotoapparat.parameter.selector.LensPositionSelectors.back;
import static io.fotoapparat.parameter.selector.SizeSelectors.biggestSize;
import static java.lang.Long.parseLong;
import static net.iGap.G.chatSendMessageUtil;
import static net.iGap.G.context;
import static net.iGap.R.id.ac_ll_parent;
import static net.iGap.R.string.item;
import static net.iGap.helper.HelperGetDataFromOtherApp.messageType;
import static net.iGap.module.AttachFile.getFilePathFromUri;
import static net.iGap.module.AttachFile.getPathN;
import static net.iGap.module.AttachFile.request_code_VIDEO_CAPTURED;
import static net.iGap.module.AttachFile.request_code_open_document;
import static net.iGap.module.AttachFile.request_code_pic_file;
import static net.iGap.module.MessageLoader.getLocalMessage;
import static net.iGap.module.enums.ProgressState.HIDE;
import static net.iGap.module.enums.ProgressState.SHOW;
import static net.iGap.proto.ProtoClientGetRoomHistory.ClientGetRoomHistory.Direction.DOWN;
import static net.iGap.proto.ProtoClientGetRoomHistory.ClientGetRoomHistory.Direction.UP;
import static net.iGap.proto.ProtoGlobal.Room.Type.CHANNEL;
import static net.iGap.proto.ProtoGlobal.Room.Type.CHAT;
import static net.iGap.proto.ProtoGlobal.Room.Type.GROUP;
import static net.iGap.proto.ProtoGlobal.RoomMessageType.CONTACT;
import static net.iGap.proto.ProtoGlobal.RoomMessageType.GIF_TEXT;
import static net.iGap.proto.ProtoGlobal.RoomMessageType.IMAGE_TEXT;
import static net.iGap.proto.ProtoGlobal.RoomMessageType.LOG;
import static net.iGap.proto.ProtoGlobal.RoomMessageType.VIDEO;
import static net.iGap.proto.ProtoGlobal.RoomMessageType.VIDEO_TEXT;

public class FragmentChat extends BaseFragment
        implements IMessageItem, OnChatClearMessageResponse, OnChatSendMessageResponse, OnChatUpdateStatusResponse, OnChatMessageSelectionChanged<AbstractMessage>, OnChatMessageRemove, OnVoiceRecord, OnUserInfoResponse, OnSetAction, OnUserUpdateStatus, OnLastSeenUpdateTiming, OnGroupAvatarResponse, OnChannelAddMessageReaction, OnChannelGetMessagesStats, OnChatDelete, OnBackgroundChanged {

    public static FinishActivity finishActivity;
    public MusicPlayer musicPlayer;
    private AttachFile attachFile;
    private EditText edtSearchMessage;
    private SharedPreferences sharedPreferences;
    private net.iGap.module.EmojiEditTextE edtChat;
    private MaterialDesignTextView imvSendButton;
    private MaterialDesignTextView imvAttachFileButton;
    private MaterialDesignTextView imvMicButton;
    //  private MaterialDesignTextView btnReplaySelected;
    private RippleView rippleDeleteSelected;
    private RippleView rippleReplaySelected;
    private ArrayList<String> listPathString;
    private MaterialDesignTextView btnCancelSendingFile;
    private ViewGroup viewGroupLastSeen;
    private CircleImageView imvUserPicture;
    private ImageView imgBackGround;
    private RecyclerView recyclerView;
    private MaterialDesignTextView imvSmileButton;
    private LocationManager locationManager;
    private OnComplete complete;
    private View viewAttachFile;
    private View viewMicRecorder;
    private VoiceRecord voiceRecord;
    private MaterialDesignTextView txtClearMessageSearch;
    private MaterialDesignTextView btnHashLayoutClose;
    private SearchHash searchHash;
    private MessagesAdapter<AbstractMessage> mAdapter;
    private ProtoGlobal.Room.Type chatType;
    private EmojiPopup emojiPopup;
    public static OnComplete onMusicListener;
    public static IUpdateLogItem iUpdateLogItem;
    private GroupChatRole groupRole;
    private ChannelChatRole channelRole;
    private PopupWindow popupWindow;
    private Uri latestUri;
    private Calendar lastDateCalendar = Calendar.getInstance();
    private MaterialDesignTextView iconMute;
    private MyAppBarLayout appBarLayout;
    private LinearLayout mediaLayout;
    private LinearLayout ll_Search;
    private LinearLayout layoutAttachBottom;
    private LinearLayout ll_attach_text;
    private LinearLayout ll_AppBarSelected;
    private LinearLayout toolbar;
    // private LinearLayout ll_navigate_Message;
    private LinearLayout ll_navigateHash;
    private LinearLayout lyt_user;
    private LinearLayout mReplayLayout;
    private ProgressBar prgWaiting;
    //  private AVLoadingIndicatorView avi;
    private ViewGroup vgSpamUser;
    private RecyclerView.OnScrollListener scrollListener;
    private RecyclerView rcvBottomSheet;
    private FrameLayout llScrollNavigate;
    private FastItemAdapter fastItemAdapter;
    private BottomSheetDialog bottomSheetDialog;
    private static List<StructBottomSheet> contacts;
    public static OnPathAdapterBottomSheet onPathAdapterBottomSheet;
    private View viewBottomSheet;
    public static OnClickCamera onClickCamera;
    private Fotoapparat fotoapparatSwitcher;
    public static OnComplete hashListener;
    public static OnComplete onComplete;
    public static OnUpdateUserOrRoomInfo onUpdateUserOrRoomInfo;
    private static ArrayMap<String, Boolean> compressedPath = new ArrayMap<>(); // keep compressedPath and also keep video path that never be won't compressed
    public static ArrayMap<Long, HelperUploadFile.StructUpload> compressingFiles = new ArrayMap<>();
    private ArrayList<StructBottomSheet> itemGalleryList = new ArrayList<StructBottomSheet>();
    private static ArrayList<StructUploadVideo> structUploadVideos = new ArrayList<>();
    private RealmRoomMessage firstUnreadMessage;
    private RealmRoomMessage firstUnreadMessageInChat; // when user is in this room received new message
    private RealmRoomMessage voiceLastMessage = null;

    public static int forwardMessageCount = 0;
    public static ArrayList<Parcelable> mForwardMessages;
    private Realm realmChat;
    public static boolean canUpdateAfterDownload = false;

    private ArrayList<StructBackGroundSeen> backGroundSeenList = new ArrayList<>();

    private TextView txtSpamUser;
    private TextView txtSpamClose;
    private TextView send;
    private TextView txtCountItem;
    private TextView txtNewUnreadMessage;
    private TextView imvCancelForward;
    private TextView btnUp;
    private TextView btnDown;
    private TextView txtChannelMute;
    // private TextView btnUpMessage;
    // private TextView btnDownMessage;
    // private TextView txtMessageCounter;
    private TextView btnUpHash;
    private TextView btnDownHash;
    private TextView txtHashCounter;
    private TextView txtFileNameForSend;
    private TextView txtNumberOfSelected;
    private EmojiTextViewE txtName;
    private TextView txtLastSeen;
    private TextView txtEmptyMessages;

    public String title;
    public String phoneNumber;
    private String userName = "";
    private String latestFilePath;
    private String mainVideoPath = "";
    private String color;
    private String initialize;
    private String groupParticipantsCountLabel;
    private String channelParticipantsCountLabel;
    private String userStatus;
    public static String titleStatic;

    private Boolean isGoingFromUserLink = false;
    private Boolean isNotJoin = false; // this value will be trued when come to this chat with username
    private boolean isCheckBottomSheet = false;
    private boolean firsInitScrollPosition = false;
    private boolean initHash = false;
    private boolean initAttach = false;
    private boolean initEmoji = false;
    private boolean hasDraft = false;
    private boolean hasForward = false;
    private boolean blockUser = false;
    private boolean isChatReadOnly = false;
    private boolean isMuteNotification;
    private boolean sendByEnter = false;

    private boolean isCloudRoom;


    private long biggestMessageId = 0;
    private long replyToMessageId = 0;
    private long userId;
    private long lastSeen;
    private long chatPeerId;
    private long userTime;
    public static long messageId;
    private long savedScrollMessageId;
    private long latestButtonClickTime; // use from this field for avoid from show button again after click it
    public long mRoomId = 0;
    public static long mRoomIdStatic = 0;

    private final int END_CHAT_LIMIT = 5;
    private int countNewMessage = 0;
    private int lastPosition = 0;
    private int unreadCount = 0;
    private int latestRequestCode;
    private int messageCounter = 0;
    private int selectedPosition = 0;
    private boolean isNoMessage = true;
    private boolean isEmojiSHow = false;
    private boolean isCameraStart = false;
    private boolean isCameraAttached = false;
    private boolean isPermissionCamera = false;

    private View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        isNeedResume = true;
        rootView = inflater.inflate(R.layout.activity_chat, container, false);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        realmChat = Realm.getDefaultInstance();

        startPageFastInitialize();
        G.handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                initMain();
            }
        }, Config.FAST_START_PAGE_TIME);
    }

    @Override
    public void onStart() {
        super.onStart();

        G.handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                RealmRoomMessage.fetchMessages(mRoomId, new OnActivityChatStart() {
                    @Override
                    public void resendMessage(RealmRoomMessage message) {
                        chatSendMessageUtil.build(chatType, message.getRoomId(), message);
                    }

                    @Override
                    public void resendMessageNeedsUpload(RealmRoomMessage message) {
                        HelperUploadFile.startUploadTaskChat(mRoomId, chatType, message.getAttachment().getLocalFilePath(), message.getMessageId(), message.getMessageType(), message.getMessage(), RealmRoomMessage.getReplyMessageId(message), new HelperUploadFile.UpdateListener() {
                            @Override
                            public void OnProgress(int progress, FileUploadStructure struct) {
                                if (canUpdateAfterDownload) {
                                    insertItemAndUpdateAfterStartUpload(progress, struct);
                                }
                            }

                            @Override
                            public void OnError() {

                            }
                        });
                    }

                    @Override
                    public void sendSeenStatus(RealmRoomMessage message) {
                        if (!isNotJoin) {
                            G.chatUpdateStatusUtil.sendUpdateStatus(chatType, mRoomId, message.getMessageId(), ProtoGlobal.RoomMessageStatus.SEEN);
                        }
                    }
                });

                // update badge count after open one chat room
                Realm realm = Realm.getDefaultInstance();
                try {
                    int unreadCount = 0;

                    RealmResults<RealmRoom> realmRooms = realm.where(RealmRoom.class).notEqualTo(RealmRoomFields.ID, mRoomId).findAll();
                    for (RealmRoom realmRoom1 : realmRooms) {
                        if (realmRoom1.getUnreadCount() > 0) {
                            unreadCount += realmRoom1.getUnreadCount();
                        }
                    }

                    ShortcutBadger.applyCount(context, unreadCount);
                } catch (Exception e) {

                    e.printStackTrace();
                }

                realm.close();
            }
        }, 500);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (FragmentShearedMedia.list != null && FragmentShearedMedia.list.size() > 0) {
            deleteSelectedMessageFromAdapter(FragmentShearedMedia.list);
            FragmentShearedMedia.list.clear();
        }

        canUpdateAfterDownload = true;

        G.handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                initLayoutHashNavigationCallback();
                showSpamBar();

                updateShowItemInScreen();


                if (isGoingFromUserLink) {
                    new RequestClientSubscribeToRoom().clientSubscribeToRoom(mRoomId);
                }

                //+final Realm updateUnreadCountRealm = Realm.getDefaultInstance();
                getRealmChat().executeTransactionAsync(new Realm.Transaction() {//ASYNC
                    @Override
                    public void execute(Realm realm) {
                        final RealmRoom room = realm.where(RealmRoom.class).equalTo(RealmRoomFields.ID, mRoomId).findFirst();
                        if (room != null) {
                            room.setUnreadCount(0);
                            realm.copyToRealmOrUpdate(room);

                            if (room.getType() != CHAT) {
                                /**
                                 * set member count
                                 * set this code in onResume for update this value when user
                                 * come back from profile activities
                                 */

                                String members = null;
                                if (room.getType() == GROUP && room.getGroupRoom() != null) {
                                    members = room.getGroupRoom().getParticipantsCountLabel();
                                } else if (room.getType() == CHANNEL && room.getChannelRoom() != null) {
                                    members = room.getChannelRoom().getParticipantsCountLabel();
                                }

                                final String finalMembers = members;
                                if (finalMembers != null) {
                                    G.handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (finalMembers != null && HelperString.isNumeric(finalMembers) && Integer.parseInt(finalMembers) == 1) {
                                                txtLastSeen.setText(finalMembers + " " + G.context.getResources().getString(R.string.one_member_chat));
                                            } else {
                                                txtLastSeen.setText(finalMembers + " " + G.context.getResources().getString(R.string.member_chat));
                                            }
                                            //    avi.setVisibility(View.GONE);

                                            if (HelperCalander.isLanguagePersian) txtLastSeen.setText(HelperCalander.convertToUnicodeFarsiNumber(txtLastSeen.getText().toString()));
                                        }
                                    });
                                }
                            } else {
                                RealmRegisteredInfo realmRegisteredInfo = realm.where(RealmRegisteredInfo.class).equalTo(RealmRegisteredInfoFields.ID, room.getChatRoom().getPeerId()).findFirst();
                                if (realmRegisteredInfo != null) {
                                    setUserStatus(realmRegisteredInfo.getStatus(), realmRegisteredInfo.getLastSeen());
                                }
                            }
                        }
                    }
                }, new Realm.Transaction.OnSuccess() {
                    @Override
                    public void onSuccess() {
                        /**
                         * hint: should use from this method here because we need checkAction
                         * state after set members count for avoid from hide action if exist
                         */
                        checkAction();

                        RealmRoom room = getRealmChat().where(RealmRoom.class).equalTo(RealmRoomFields.ID, mRoomId).findFirst();
                        if (room != null) {
                            if (txtName == null) {
                                txtName = (EmojiTextViewE) rootView.findViewById(R.id.chl_txt_name);
                            }
                            txtName.setText(room.getTitle());
                        }

                        //updateUnreadCountRealm.close();
                    }
                });

                MusicPlayer.chatLayout = mediaLayout;
                ActivityCall.stripLayoutChat = rootView.findViewById(R.id.ac_ll_strip_call);

                ActivityMain.setMediaLayout();
                ActivityMain.setStripLayoutCall();

                if (!G.twoPaneMode) {
                    try {
                        if (G.fragmentActivity != null) {
                            ((ActivityMain) G.fragmentActivity).lockNavigation();
                        }
                    } catch (Exception e) {
                        HelperLog.setErrorLog("fragment chat ondestroy   " + e.toString());
                    }
                }

            }
        }, Config.LOW_START_PAGE_TIME);

        mRoomIdStatic = mRoomId;
        titleStatic = title;

        G.clearMessagesUtil.setOnChatClearMessageResponse(this);
        G.onUserInfoResponse = this;
        G.onChannelAddMessageReaction = this;
        G.onChannelGetMessagesStats = this;
        G.onSetAction = this;
        G.onUserUpdateStatus = this;
        G.onLastSeenUpdateTiming = this;
        G.onChatDelete = this;
        G.onBackgroundChanged = this;
        G.helperNotificationAndBadge.cancelNotification();

        finishActivity = new FinishActivity() {
            @Override
            public void finishActivity() {
                // ActivityChat.this.finish();
                finishChat();
            }
        };

        initCallbacks();
        HelperNotificationAndBadge.isChatRoomNow = true;

        onUpdateUserOrRoomInfo = new OnUpdateUserOrRoomInfo() {
            @Override
            public void onUpdateUserOrRoomInfo(final String messageId) {

                if (messageId != null && messageId.length() > 0) {
                    G.handler.post(new Runnable() {
                        @Override
                        public void run() {
                            int start = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
                            for (int i = start; i < mAdapter.getItemCount() && i < start + 15; i++) {
                                try {
                                    if (mAdapter.getItem(i).mMessage != null && mAdapter.getItem(i).mMessage.messageID.equals(messageId)) {
                                        mAdapter.notifyItemChanged(i);
                                        break;
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                }
            }
        };

        if (backGroundSeenList != null && backGroundSeenList.size() > 0) {
            for (int i = 0; i < backGroundSeenList.size(); i++) {

                G.chatUpdateStatusUtil.sendUpdateStatus(backGroundSeenList.get(i).roomType, mRoomId, backGroundSeenList.get(i).messageID, ProtoGlobal.RoomMessageStatus.SEEN);
            }

            backGroundSeenList.clear();
        }

        if (G.isInCall) {
            rootView.findViewById(R.id.ac_ll_strip_call).setVisibility(View.VISIBLE);

            ActivityCall.txtTimeChat = (TextView) rootView.findViewById(R.id.cslcs_txt_timer);

            TextView txtCallActivityBack = (TextView) rootView.findViewById(R.id.cslcs_btn_call_strip);
            txtCallActivityBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(G.fragmentActivity, ActivityCall.class));
                }
            });

            G.iCallFinishChat = new ICallFinish() {
                @Override
                public void onFinish() {
                    try {
                        rootView.findViewById(R.id.ac_ll_strip_call).setVisibility(View.GONE);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
        } else {
            rootView.findViewById(R.id.ac_ll_strip_call).setVisibility(View.GONE);
        }

        if (isCloudRoom) {
            rootView.findViewById(R.id.ac_txt_cloud).setVisibility(View.VISIBLE);
            rootView.findViewById(R.id.chl_imv_user_picture).setVisibility(View.GONE);
        } else {
            setAvatar();
        }

        if (mForwardMessages == null) {
            rootView.findViewById(R.id.ac_ll_forward).setVisibility(View.GONE);
        }

        registerListener();
    }

    @Override
    public void onPause() {
        storingLastPosition();
        super.onPause();
        if (isGoingFromUserLink && isNotJoin) {
            new RequestClientUnsubscribeFromRoom().clientUnsubscribeFromRoom(mRoomId);
        }
        onMusicListener = null;
        iUpdateLogItem = null;



        unRegisterListener();
    }

    @Override
    public void onStop() {

        canUpdateAfterDownload = false;

        setDraft();
        HelperNotificationAndBadge.isChatRoomNow = false;

        //if (isNotJoin) { // hint : commented this code, because when going to profile and return can't load message
        //
        //    /**
        //     * delete all  deleted row from database
        //     */
        //    RealmRoom.deleteRoom(mRoomId);
        //}


        // room id have to be set to default, otherwise I'm in the room always!

        MusicPlayer.chatLayout = null;
        ActivityCall.stripLayoutChat = null;



        super.onStop();
    }

    @Override
    public void onDestroy() {

        super.onDestroy();

        mRoomId = -1;

        if (G.fragmentActivity != null) {
            ((ActivityMain) G.fragmentActivity).resume();
        }


        if (realmChat != null && !realmChat.isClosed()) {
            realmChat.close();
        }

    }


    private void registerListener() {

        G.dispatchTochEventChat = new IDispatchTochEvent() {
            @Override
            public void getToch(MotionEvent event) {
                if (voiceRecord != null) {
                    voiceRecord.dispatchTouchEvent(event);
                }
            }
        };

        G.onBackPressedChat = new IOnBackPressed() {
            @Override
            public boolean onBack() {
                return onBackPressed();
            }
        };

        G.iSendPositionChat = new ISendPosition() {
            @Override
            public void send(Double latitude, Double longitude, String imagePath) {
                sendPosition(latitude, longitude, imagePath);
            }
        };
    }

    private void unRegisterListener() {

        G.dispatchTochEventChat = null;
        G.onBackPressedChat = null;
        G.iSendPositionChat = null;
    }

    public boolean onBackPressed() {

        boolean stopSuperPress = true;

        FragmentShowImage fragment = (FragmentShowImage) G.fragmentActivity.getSupportFragmentManager().findFragmentByTag(FragmentShowImage.class.getName());
        if (fragment != null) {
            removeFromBaseFragment(fragment);
        } else if (mAdapter != null && mAdapter.getSelections().size() > 0) {
            mAdapter.deselect();
        } else if (emojiPopup != null && emojiPopup.isShowing()) {
            emojiPopup.dismiss();
        } else {
            return false;
        }

        return stopSuperPress;
    }

    @Override
    public void onActivityResult(final int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_CANCELED) {
            HelperSetAction.sendCancel(messageId);

            if (prgWaiting != null) {
                prgWaiting.setVisibility(View.GONE);
            }
        }

        if (requestCode == AttachFile.request_code_position && locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            try {
                attachFile.requestGetPosition(complete, FragmentChat.this);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }

        if (resultCode == Activity.RESULT_OK) {

            HelperSetAction.sendCancel(messageId);

            if (requestCode == AttachFile.request_code_contact_phone) {
                latestUri = data.getData();
                sendMessage(requestCode, "");
                return;
            }

            listPathString = null;
            if (AttachFile.request_code_TAKE_PICTURE == requestCode) {

                listPathString = new ArrayList<>();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    listPathString.add(AttachFile.mCurrentPhotoPath);
                } else {
                    listPathString.add(AttachFile.imagePath);
                }

                latestUri = null; // check
            } else if (request_code_VIDEO_CAPTURED == requestCode) {

                listPathString = new ArrayList<>();
                listPathString.add(AttachFile.videoPath);

                latestUri = null; // check
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    if (data.getClipData() != null) { // multi select file
                        listPathString = attachFile.getClipData(data.getClipData());

                        if (listPathString != null) {
                            for (int i = 0; i < listPathString.size(); i++) {
                                if (listPathString.get(i) != null) {
                                    listPathString.set(i, getFilePathFromUri(Uri.fromFile(new File(listPathString.get(i)))));
                                }
                            }
                        }
                    }
                }

                if (listPathString == null || listPathString.size() < 1) {
                    listPathString = new ArrayList<>();

                    if (data.getData() != null) {
                        listPathString.add(getFilePathFromUri(data.getData()));
                    }
                }
            }
            latestRequestCode = requestCode;

            /**
             * compress video if BuildVersion is bigger that 18
             */
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                if (requestCode == request_code_VIDEO_CAPTURED) {
                    if (sharedPreferences.getInt(SHP_SETTING.KEY_TRIM, 1) == 1) {
                        Intent intent = new Intent(G.fragmentActivity, ActivityTrimVideo.class);
                        intent.putExtra("PATH", listPathString.get(0));
                        startActivityForResult(intent, AttachFile.request_code_trim_video);
                        return;
                    } else if (sharedPreferences.getInt(SHP_SETTING.KEY_COMPRESS, 1) == 1) {

                        File mediaStorageDir = new File(G.DIR_VIDEOS);
                        File mediaFile = new File(mediaStorageDir.getPath() + File.separator + "video_" + HelperString.getRandomFileName(3) + ".mp4");
                        listPathString = new ArrayList<>();

                        Uri uri = data.getData();
                        File tempFile = com.lalongooo.videocompressor.file.FileUtils.saveTempFile(HelperString.getRandomFileName(5), G.fragmentActivity, uri);
                        mainVideoPath = tempFile.getPath();
                        String savePathVideoCompress = Environment.getExternalStorageDirectory() + File.separator + com.lalongooo.videocompressor.Config.VIDEO_COMPRESSOR_APPLICATION_DIR_NAME + com.lalongooo.videocompressor.Config.VIDEO_COMPRESSOR_COMPRESSED_VIDEOS_DIR + "VIDEO_" + new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date()) + ".mp4";

                        listPathString.add(savePathVideoCompress);

                        new VideoCompressor().execute(tempFile.getPath(), savePathVideoCompress);
                        showDraftLayout();
                        setDraftMessage(requestCode);
                        latestRequestCode = requestCode;
                        return;
                    } else {
                        compressedPath.put(listPathString.get(0), true);
                    }
                }

                if (requestCode == AttachFile.request_code_trim_video) {
                    latestRequestCode = request_code_VIDEO_CAPTURED;
                    showDraftLayout();
                    setDraftMessage(request_code_VIDEO_CAPTURED);
                    if ((sharedPreferences.getInt(SHP_SETTING.KEY_COMPRESS, 1) == 1)) {
                        File mediaStorageDir = new File(G.DIR_VIDEOS);
                        listPathString = new ArrayList<>();

                        String savePathVideoCompress = Environment.getExternalStorageDirectory() + File.separator + com.lalongooo.videocompressor.Config.VIDEO_COMPRESSOR_APPLICATION_DIR_NAME + com.lalongooo.videocompressor.Config.VIDEO_COMPRESSOR_COMPRESSED_VIDEOS_DIR + "VIDEO_" + new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date()) + ".mp4";

                        listPathString.add(savePathVideoCompress);
                        mainVideoPath = data.getData().getPath();
                        new VideoCompressor().execute(data.getData().getPath(), savePathVideoCompress);
                    } else {
                        compressedPath.put(data.getData().getPath(), true);
                    }
                    return;
                }
            }

            if (listPathString.size() == 1) {
                /**
                 * compress video if BuildVersion is bigger that 18
                 */
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    if (requestCode == AttachFile.requestOpenGalleryForVideoMultipleSelect) {
                        if (sharedPreferences.getInt(SHP_SETTING.KEY_TRIM, 1) == 1) {
                            Intent intent = new Intent(G.fragmentActivity, ActivityTrimVideo.class);
                            intent.putExtra("PATH", listPathString.get(0));
                            startActivityForResult(intent, AttachFile.request_code_trim_video);
                            return;
                        } else if ((sharedPreferences.getInt(SHP_SETTING.KEY_COMPRESS, 1) == 1)) {

                            mainVideoPath = listPathString.get(0);

                            String savePathVideoCompress = Environment.getExternalStorageDirectory() + File.separator + com.lalongooo.videocompressor.Config.VIDEO_COMPRESSOR_APPLICATION_DIR_NAME + com.lalongooo.videocompressor.Config.VIDEO_COMPRESSOR_COMPRESSED_VIDEOS_DIR + "VIDEO_" + new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date()) + ".mp4";

                            listPathString.set(0, savePathVideoCompress);

                            new VideoCompressor().execute(mainVideoPath, savePathVideoCompress);

                            showDraftLayout();
                            setDraftMessage(requestCode);
                        } else {
                            compressedPath.put(listPathString.get(0), true);
                        }
                    }
                    showDraftLayout();
                    setDraftMessage(requestCode);
                } else {
                    /**
                     * set compressed true for use this path
                     */
                    compressedPath.put(listPathString.get(0), true);

                    showDraftLayout();
                    setDraftMessage(requestCode);
                }
            } else if (listPathString.size() > 1) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        for (final String path : listPathString) {
                            /**
                             * set compressed true for use this path
                             */
                            compressedPath.put(path, true);

                            G.handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    if (requestCode == AttachFile.requestOpenGalleryForImageMultipleSelect && !path.toLowerCase().endsWith(".gif")) {
                                        String localPathNew = attachFile.saveGalleryPicToLocal(path);
                                        sendMessage(requestCode, localPathNew);
                                    } else {
                                        sendMessage(requestCode, path);
                                    }
                                }
                            });
                        }
                    }
                }).start();
            }

            if (listPathString.size() == 1 && listPathString.get(0) != null) {

                if (sharedPreferences.getInt(SHP_SETTING.KEY_CROP, 1) == 1) {

                    if (requestCode == AttachFile.requestOpenGalleryForImageMultipleSelect) {
                        if (!listPathString.get(0).toLowerCase().endsWith(".gif")) {

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                listPathString.set(0, attachFile.saveGalleryPicToLocal(listPathString.get(0)));

                                Uri uri = Uri.parse(listPathString.get(0));
                                Intent intent = new Intent(G.fragmentActivity, ActivityCrop.class);
                                intent.putExtra("IMAGE_CAMERA", AttachFile.getFilePathFromUriAndCheckForAndroid7(uri, HelperGetDataFromOtherApp.FileType.image));
                                intent.putExtra("TYPE", "gallery");
                                intent.putExtra("PAGE", "chat");
                                startActivityForResult(intent, IntentRequests.REQ_CROP);

                                G.handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (prgWaiting != null) {
                                            prgWaiting.setVisibility(View.GONE);
                                        }
                                    }
                                });
                            } else {
                                listPathString.set(0, attachFile.saveGalleryPicToLocal(listPathString.get(0)));
                                Intent intent = new Intent(G.fragmentActivity, ActivityCrop.class);
                                Uri uri = Uri.parse(listPathString.get(0));
                                uri = Uri.parse("file://" + getFilePathFromUri(uri));
                                intent.putExtra("IMAGE_CAMERA", uri.toString());
                                intent.putExtra("TYPE", "gallery");
                                intent.putExtra("PAGE", "chat");
                                startActivityForResult(intent, IntentRequests.REQ_CROP);

                                G.handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (prgWaiting != null) {
                                            prgWaiting.setVisibility(View.GONE);
                                        }
                                    }
                                });
                            }
                        } else {
                            G.handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    if (prgWaiting != null) {
                                        prgWaiting.setVisibility(View.GONE);
                                    }
                                }
                            });
                        }
                    } else if (requestCode == AttachFile.request_code_TAKE_PICTURE) {

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

                            ImageHelper.correctRotateImage(listPathString.get(0), true);
                            Intent intent = new Intent(G.fragmentActivity, ActivityCrop.class);

                            intent.putExtra("IMAGE_CAMERA", listPathString.get(0));
                            intent.putExtra("TYPE", "camera");
                            intent.putExtra("PAGE", "chat");
                            startActivityForResult(intent, IntentRequests.REQ_CROP);

                            G.handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    if (prgWaiting != null) {
                                        prgWaiting.setVisibility(View.GONE);
                                    }
                                }
                            });
                        } else {
                            ImageHelper.correctRotateImage(listPathString.get(0), true);

                            Intent intent = new Intent(G.fragmentActivity, ActivityCrop.class);

                            intent.putExtra("IMAGE_CAMERA", "file://" + listPathString.get(0));
                            intent.putExtra("TYPE", "camera");
                            intent.putExtra("PAGE", "chat");
                            startActivityForResult(intent, IntentRequests.REQ_CROP);

                            G.handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    if (prgWaiting != null) {
                                        prgWaiting.setVisibility(View.GONE);
                                    }
                                }
                            });
                        }
                    }
                } else {

                    if (requestCode == AttachFile.request_code_TAKE_PICTURE) {

                        Thread thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                ImageHelper.correctRotateImage(listPathString.get(0), true);
                            }
                        });
                        thread.start();
                    } else if (requestCode == AttachFile.requestOpenGalleryForImageMultipleSelect && !listPathString.get(0).toLowerCase().endsWith(".gif")) {
                        Thread thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                listPathString.set(0, attachFile.saveGalleryPicToLocal(listPathString.get(0)));
                            }
                        });
                        thread.start();
                    }
                }
            }
        }
    }

    /**
     * set just important item to view in onCreate and load another objects in onResume
     * actions : set app color, load avatar, set background, set title, set status chat or member for group or channel
     */
    private void startPageFastInitialize() {

        attachFile = new AttachFile(G.fragmentActivity);
        backGroundSeenList.clear();

        //+Realm realm = Realm.getDefaultInstance();

        Bundle extras = getArguments();
        if (extras != null) {
            mRoomId = extras.getLong("RoomId");
            chatPeerId = extras.getLong("peerId");

            RealmRoom realmRoom = getRealmChat().where(RealmRoom.class).equalTo(RealmRoomFields.ID, mRoomId).findFirst();
            pageSettings();

            // avi = (AVLoadingIndicatorView)  rootView.findViewById(R.id.avi);
            txtName = (EmojiTextViewE) rootView.findViewById(R.id.chl_txt_name);
            txtLastSeen = (TextView) rootView.findViewById(R.id.chl_txt_last_seen);
            viewGroupLastSeen = (ViewGroup) rootView.findViewById(R.id.chl_txt_viewGroup_seen);
            imvUserPicture = (CircleImageView) rootView.findViewById(R.id.chl_imv_user_picture);
            /**
             * need this info for load avatar
             */
            if (realmRoom != null) {
                chatType = realmRoom.getType();
                if (chatType == CHAT) {
                    chatPeerId = realmRoom.getChatRoom().getPeerId();
                    RealmRegisteredInfo realmRegisteredInfo = getRealmChat().where(RealmRegisteredInfo.class).equalTo(RealmRegisteredInfoFields.ID, chatPeerId).findFirst();
                    if (realmRegisteredInfo != null) {
                        title = realmRegisteredInfo.getDisplayName();
                        lastSeen = realmRegisteredInfo.getLastSeen();
                        userStatus = realmRegisteredInfo.getStatus();
                    } else {
                        /**
                         * when userStatus isn't EXACTLY lastSeen time not used so don't need
                         * this time and also this time not exist in room info
                         */
                        title = realmRoom.getTitle();
                        userStatus = G.context.getResources().getString(R.string.last_seen_recently);
                    }
                } else {
                    mRoomId = realmRoom.getId();
                    title = realmRoom.getTitle();
                    if (chatType == GROUP) {
                        groupParticipantsCountLabel = realmRoom.getGroupRoom().getParticipantsCountLabel();
                    } else {
                        groupParticipantsCountLabel = realmRoom.getChannelRoom().getParticipantsCountLabel();
                    }
                }

                if (chatType == CHAT) {
                    setUserStatus(userStatus, lastSeen);
                } else if ((chatType == GROUP) || (chatType == CHANNEL)) {
                    if (groupParticipantsCountLabel != null) {

                        if (HelperString.isNumeric(groupParticipantsCountLabel) && Integer.parseInt(groupParticipantsCountLabel) == 1) {
                            txtLastSeen.setText(groupParticipantsCountLabel + " " + G.context.getResources().getString(R.string.one_member_chat));
                        } else {
                            txtLastSeen.setText(groupParticipantsCountLabel + " " + G.context.getResources().getString(R.string.member_chat));
                        }
                        // avi.setVisibility(View.GONE);
                        ViewMaker.setLayoutDirection(viewGroupLastSeen, View.LAYOUT_DIRECTION_LTR);
                    }
                }
            } else if (chatPeerId != 0) {
                /**
                 * when user start new chat this block will be called
                 */
                chatType = CHAT;
                RealmRegisteredInfo realmRegisteredInfo = getRealmChat().where(RealmRegisteredInfo.class).equalTo(RealmRegisteredInfoFields.ID, chatPeerId).findFirst();
                title = realmRegisteredInfo.getDisplayName();
                lastSeen = realmRegisteredInfo.getLastSeen();
                userStatus = realmRegisteredInfo.getStatus();
                setUserStatus(userStatus, lastSeen);
            }

            if (title != null) {
                txtName.setText(title);
            }
            /**
             * change english number to persian number
             */
            if (HelperCalander.isLanguagePersian) {
                txtName.setText(txtName.getText().toString());
            }
            if (HelperCalander.isLanguagePersian) {
                txtLastSeen.setText(HelperCalander.convertToUnicodeFarsiNumber(txtLastSeen.getText().toString()));
            }
        }

        /**
         * hint: don't check isCloudRoom with (( RealmRoom.isCloudRoom(mRoomId, realm); ))
         * because in first time room not exist in RealmRoom and value is false always.
         * so just need to check this value with chatPeerId
         */
        if (chatPeerId == G.userId) {
            isCloudRoom = true;
        }

        //+realm.close();
    }

    private void initMain() {
        HelperGetMessageState.clearMessageViews();

        /**
         * define views
         */
        mediaLayout = (LinearLayout) rootView.findViewById(R.id.ac_ll_music_layout);
        MusicPlayer.setMusicPlayer(mediaLayout);

        lyt_user = (LinearLayout) rootView.findViewById(R.id.lyt_user);
        viewAttachFile = rootView.findViewById(R.id.layout_attach_file);
        viewMicRecorder = rootView.findViewById(R.id.layout_mic_recorde);
        prgWaiting = (ProgressBar) rootView.findViewById(R.id.chl_prgWaiting);
        AppUtils.setProgresColler(prgWaiting);
        voiceRecord = new VoiceRecord(G.fragmentActivity, viewMicRecorder, viewAttachFile, this);

        prgWaiting.setVisibility(View.VISIBLE);

        txtEmptyMessages = (TextView) rootView.findViewById(R.id.empty_messages);

        lastDateCalendar.clear();

        locationManager = (LocationManager) G.fragmentActivity.getSystemService(LOCATION_SERVICE);

        Bundle extras = getArguments();
        if (extras != null) {
            mRoomId = extras.getLong("RoomId");
            isGoingFromUserLink = extras.getBoolean("GoingFromUserLink");
            isNotJoin = extras.getBoolean("ISNotJoin");
            userName = extras.getString("UserName");

            if (isNotJoin) {
                final LinearLayout layoutJoin = (LinearLayout) rootView.findViewById(R.id.ac_ll_join);
                final RelativeLayout layoutMute = (RelativeLayout) rootView.findViewById(R.id.chl_ll_channel_footer);
                layoutJoin.setBackgroundColor(Color.parseColor(G.appBarColor));
                layoutJoin.setVisibility(View.VISIBLE);
                layoutMute.setVisibility(View.GONE);
                layoutJoin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        HelperUrl.showIndeterminateProgressDialog();
                        G.onClientJoinByUsername = new OnClientJoinByUsername() {
                            @Override
                            public void onClientJoinByUsernameResponse() {

                                isNotJoin = false;
                                HelperUrl.closeDialogWaiting();

                                G.handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        layoutJoin.setVisibility(View.GONE);
                                        if (chatType == CHANNEL) {
                                            layoutMute.setVisibility(View.VISIBLE);
                                        }
                                        rootView.findViewById(ac_ll_parent).invalidate();

                                        if (chatType == GROUP) {
                                            viewAttachFile.setVisibility(View.VISIBLE);
                                            isChatReadOnly = false;
                                        }

                                        final RealmRoom joinedRoom = getRealmChat().where(RealmRoom.class).equalTo(RealmRoomFields.ID, mRoomId).findFirst();
                                        if (joinedRoom != null) {
                                            getRealmChat().executeTransaction(new Realm.Transaction() {
                                                @Override
                                                public void execute(Realm realm) {
                                                    joinedRoom.setDeleted(false);
                                                    if (joinedRoom.getType() == GROUP) {
                                                        joinedRoom.setReadOnly(false);
                                                    }
                                                }
                                            });
                                        }
                                    }
                                });
                                //+Realm realm = Realm.getDefaultInstance();
                                //realm.close();
                            }

                            @Override
                            public void onError(int majorCode, int minorCode) {
                                HelperUrl.dialogWaiting.dismiss();
                            }
                        };

                        /**
                         * if user joined to this room set lastMessage for that
                         */
                        //+Realm realm = Realm.getDefaultInstance();
                        final RealmResults<RealmRoomMessage> realmRoomMessages = getRealmChat().where(RealmRoomMessage.class).equalTo(RealmRoomMessageFields.ROOM_ID, mRoomId).findAllSorted(RealmRoomMessageFields.MESSAGE_ID, Sort.DESCENDING);
                        if (realmRoomMessages.size() > 0 && realmRoomMessages.first() != null) {
                            getRealmChat().executeTransaction(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    RealmRoom realmRoom = realm.where(RealmRoom.class).equalTo(RealmRoomFields.ID, mRoomId).findFirst();
                                    if (realmRoom != null) {
                                        realmRoom.setLastMessage(realmRoomMessages.first());
                                    }
                                }
                            });
                        }
                        //realm.close();

                        new RequestClientJoinByUsername().clientJoinByUsername(userName);
                    }
                });
            }
            messageId = extras.getLong("MessageId");

            /**
             * get userId . use in chat set action.
             */

            //+Realm realm = Realm.getDefaultInstance();

            RealmUserInfo realmUserInfo = getRealmChat().where(RealmUserInfo.class).findFirst();
            if (realmUserInfo == null) {
                //finish();
                finishChat();
                return;
            }
            userId = realmUserInfo.getUserId();

            RealmRoom realmRoom = getRealmChat().where(RealmRoom.class).equalTo(RealmRoomFields.ID, mRoomId).findFirst();

            if (realmRoom != null) { // room exist

                title = realmRoom.getTitle();
                initialize = realmRoom.getInitials();
                color = realmRoom.getColor();
                isChatReadOnly = realmRoom.getReadOnly();
                unreadCount = realmRoom.getUnreadCount();
                firstUnreadMessage = realmRoom.getFirstUnreadMessage();
                savedScrollMessageId = realmRoom.getLastScrollPositionMessageId();

                if (isChatReadOnly) {
                    viewAttachFile.setVisibility(View.GONE);
                    (rootView.findViewById(R.id.chl_recycler_view_chat)).setPadding(0, 0, 0, 0);
                }

                if (chatType == CHAT) {

                    RealmRegisteredInfo realmRegisteredInfo = getRealmChat().where(RealmRegisteredInfo.class).equalTo(RealmRegisteredInfoFields.ID, chatPeerId).findFirst();
                    if (realmRegisteredInfo != null) {
                        initialize = realmRegisteredInfo.getInitials();
                        color = realmRegisteredInfo.getColor();
                        phoneNumber = realmRegisteredInfo.getPhoneNumber();
                    } else {
                        title = realmRoom.getTitle();
                        initialize = realmRoom.getInitials();
                        color = realmRoom.getColor();
                        userStatus = G.context.getResources().getString(R.string.last_seen_recently);
                    }
                } else if (chatType == GROUP) {
                    RealmGroupRoom realmGroupRoom = realmRoom.getGroupRoom();
                    groupRole = realmGroupRoom.getRole();
                    groupParticipantsCountLabel = realmGroupRoom.getParticipantsCountLabel();
                } else if (chatType == CHANNEL) {
                    RealmChannelRoom realmChannelRoom = realmRoom.getChannelRoom();
                    channelRole = realmChannelRoom.getRole();
                    channelParticipantsCountLabel = realmChannelRoom.getParticipantsCountLabel();
                }
            } else {
                chatPeerId = extras.getLong("peerId");
                chatType = CHAT;
                RealmRegisteredInfo realmRegisteredInfo = getRealmChat().where(RealmRegisteredInfo.class).equalTo(RealmRegisteredInfoFields.ID, chatPeerId).findFirst();
                if (realmRegisteredInfo != null) {
                    title = realmRegisteredInfo.getDisplayName();
                    initialize = realmRegisteredInfo.getInitials();
                    color = realmRegisteredInfo.getColor();
                    lastSeen = realmRegisteredInfo.getLastSeen();
                    userStatus = realmRegisteredInfo.getStatus();
                }
            }

            //realm.close();
        }

        initAppbarSelected();
        initComponent();
        getDraft();
        getUserInfo();
        insertShearedData();
    }

    /**
     * get settings state and change view
     */
    private void pageSettings() {
        /**
         * get sendByEnter action from setting value
         */
        sharedPreferences = G.fragmentActivity.getSharedPreferences(SHP_SETTING.FILE_NAME, MODE_PRIVATE);
        sendByEnter = sharedPreferences.getInt(SHP_SETTING.KEY_SEND_BT_ENTER, 0) == 1;

        /**
         * set background
         */

        recyclerView = (RecyclerView) rootView.findViewById(R.id.chl_recycler_view_chat);

        String backGroundPath = sharedPreferences.getString(SHP_SETTING.KEY_PATH_CHAT_BACKGROUND, "");
        if (backGroundPath.length() > 0) {
            imgBackGround = (ImageView) rootView.findViewById(R.id.chl_img_view_chat);

            File f = new File(backGroundPath);
            if (f.exists()) {
                Drawable d = Drawable.createFromPath(f.getAbsolutePath());
                imgBackGround.setImageDrawable(d);
            }
        }

        /**
         * set app color to appBar
         */
        appBarLayout = (MyAppBarLayout) rootView.findViewById(R.id.ac_appBarLayout);
        appBarLayout.setBackgroundColor(Color.parseColor(G.appBarColor));
    }

    /**
     * initialize some callbacks that used in this page
     */
    public void initCallbacks() {
        chatSendMessageUtil.setOnChatSendMessageResponseChatPage(this);
        G.chatUpdateStatusUtil.setOnChatUpdateStatusResponse(this);

        G.onChatSendMessage = new OnChatSendMessage() {
            @Override
            public void Error(int majorCode, int minorCode, final int waitTime) {
                G.handler.post(new Runnable() {
                    @Override
                    public void run() {
                        showErrorDialog(waitTime);
                    }
                });
            }
        };

        G.onChatEditMessageResponse = new OnChatEditMessageResponse() {
            @Override
            public void onChatEditMessage(long roomId, final long messageId, long messageVersion, final String message, ProtoResponse.Response response) {
                if (mRoomId == roomId && mAdapter != null) {
                    // I'm in the room
                    G.handler.post(new Runnable() {
                        @Override
                        public void run() {
                            // update message text in adapter
                            mAdapter.updateMessageText(messageId, message);
                        }
                    });
                }
            }

            @Override
            public void onError(int majorCode, int minorCode) {

            }
        };

        G.onChatDeleteMessageResponse = new OnChatDeleteMessageResponse() {
            @Override
            public void onChatDeleteMessage(long deleteVersion, final long messageId, long roomId, ProtoResponse.Response response) {
                if (response.getId().isEmpty()) { // another account deleted this message

                    //Realm realm = Realm.getDefaultInstance();
                    //RealmRoomMessage roomMessage = realm.where(RealmRoomMessage.class).equalTo(RealmRoomMessageFields.MESSAGE_ID, messageId).findFirst();
                    //if (roomMessage != null) {
                    //    roomMessage.setDeleted(true);
                    //}
                    //realm.close();

                    // if deleted message is for current room clear from adapter
                    if (roomId == mRoomId) {
                        G.handler.post(new Runnable() {
                            @Override
                            public void run() {
                                // remove deleted message from adapter
                                if (mAdapter == null) {
                                    return;
                                }

                                mAdapter.removeMessage(messageId);

                                // remove tag from edtChat if the message has deleted
                                if (edtChat.getTag() != null && edtChat.getTag() instanceof StructMessageInfo) {
                                    if (Long.toString(messageId).equals(((StructMessageInfo) edtChat.getTag()).messageID)) {
                                        edtChat.setTag(null);
                                    }
                                }
                            }
                        });
                    }
                }
            }

            @Override
            public void onError(int majorCode, int minorCode) {

            }
        };

        /**
         * call from ActivityGroupProfile for update group member number or clear history
         */
        onComplete = new OnComplete() {
            @Override
            public void complete(boolean result, String messageOne, String MessageTow) {
                clearHistory(parseLong(messageOne));
            }
        };

        onMusicListener = new OnComplete() {
            @Override
            public void complete(boolean result, String messageID, String beforMessageID) {

                if (result) {
                    updateShowItemInScreen();
                } else {
                    onPlayMusic(messageID);
                }
            }
        };

        iUpdateLogItem = new IUpdateLogItem() {
            @Override
            public void onUpdate(String logText, long messageId) {

                for (int i = mAdapter.getAdapterItemCount() - 1; i >= 0; i--) {

                    try {
                        AbstractMessage item = mAdapter.getAdapterItem(i);

                        if (item.mMessage != null && item.mMessage.messageID.equals(messageId + "")) {
                            item.mMessage.messageText = logText;
                            mAdapter.notifyAdapterItemChanged(i);
                            break;
                        }
                    } catch (Exception e) {
                        Log.e("ddddd", "activity chat iUpdateLogItem    " + e.toString());
                    }
                }
            }
        };

        /**
         * after get position from gps
         */
        complete = new OnComplete() {
            @Override
            public void complete(boolean result, final String messageOne, String MessageTow) {
                try {
                    String[] split = messageOne.split(",");
                    Double latitude = Double.parseDouble(split[0]);
                    Double longitude = Double.parseDouble(split[1]);
                    FragmentMap fragment = FragmentMap.getInctance(latitude, longitude, FragmentMap.Mode.sendPosition);
                    new HelperFragment(fragment).setReplace(false).load();
                } catch (Exception e) {
                    HelperLog.setErrorLog("Activity Chat   complete   " + e.toString());
                }
            }
        };

        G.onHelperSetAction = new OnHelperSetAction() {
            @Override
            public void onAction(ProtoGlobal.ClientAction ClientAction) {
                HelperSetAction.setActionFiles(mRoomId, messageId, ClientAction, chatType);
            }
        };

        G.onClearChatHistory = new OnClearChatHistory() {
            @Override
            public void onClearChatHistory() {
                G.handler.post(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.clear();
                        recyclerView.removeAllViews();

                        /**
                         * remove tag from edtChat if the message has deleted
                         */
                        if (edtChat.getTag() != null && edtChat.getTag() instanceof StructMessageInfo) {
                            edtChat.setTag(null);
                        }
                    }
                });
            }

            @Override
            public void onError(int majorCode, int minorCode) {

            }
        };

        G.onDeleteChatFinishActivity = new OnDeleteChatFinishActivity() {
            @Override
            public void onFinish() {
                G.handler.post(new Runnable() {
                    @Override
                    public void run() {
                        //finish();
                        finishChat();
                    }
                });
            }
        };

        G.onUpdateUserStatusInChangePage = new OnUpdateUserStatusInChangePage() {
            @Override
            public void updateStatus(long peerId, String status, long lastSeen) {
                if (chatType == CHAT) {
                    setUserStatus(status, lastSeen);

                    if (chatType == CHAT) {
                        new RequestUserInfo().userInfo(peerId);
                    }
                }
            }
        };
    }

    private void initComponent() {
        toolbar = (LinearLayout) rootView.findViewById(R.id.toolbar);
        iconMute = (MaterialDesignTextView) rootView.findViewById(R.id.imgMutedRoom);
        RippleView rippleBackButton = (RippleView) rootView.findViewById(R.id.chl_ripple_back_Button);

        //+final Realm realm = Realm.getDefaultInstance();
        final RealmRoom realmRoom = getRealmChat().where(RealmRoom.class).equalTo(RealmRoomFields.ID, mRoomId).findFirst();
        if (realmRoom != null) {

            iconMute.setVisibility(realmRoom.getMute() ? View.VISIBLE : View.GONE);
            isMuteNotification = realmRoom.getMute();
        }

        if (chatType == CHAT && !isChatReadOnly) {

            if (G.userId != chatPeerId) {

                RippleView rippleCall = (RippleView) rootView.findViewById(R.id.acp_ripple_call);
                // gone or visible view call
                RealmCallConfig callConfig = getRealmChat().where(RealmCallConfig.class).findFirst();
                if (callConfig != null) {
                    if (callConfig.isVoice_calling()) {
                        rippleCall.setVisibility(View.VISIBLE);
                        rippleCall.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
                            @Override
                            public void onComplete(RippleView rippleView) {

                                FragmentCall.call(chatPeerId, false);
                            }
                        });
                    } else {
                        rippleCall.setVisibility(View.GONE);
                    }
                } else {
                    new RequestSignalingGetConfiguration().signalingGetConfiguration();
                }
            }
        }

        ll_attach_text = (LinearLayout) rootView.findViewById(R.id.ac_ll_attach_text);
        txtFileNameForSend = (TextView) rootView.findViewById(R.id.ac_txt_file_neme_for_sending);
        btnCancelSendingFile = (MaterialDesignTextView) rootView.findViewById(R.id.ac_btn_cancel_sending_file);
        btnCancelSendingFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ll_attach_text.setVisibility(View.GONE);
                edtChat.setFilters(new InputFilter[]{});
                edtChat.setText(edtChat.getText());
                edtChat.setSelection(edtChat.getText().length());

                if (edtChat.getText().length() == 0) {

                    layoutAttachBottom.animate().alpha(1F).setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            layoutAttachBottom.setVisibility(View.VISIBLE);
                        }
                    }).start();
                    imvSendButton.animate().alpha(0F).setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            G.handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    imvSendButton.clearAnimation();
                                    imvSendButton.setVisibility(View.GONE);
                                }
                            });
                        }
                    }).start();
                }
            }
        });

        // final int screenWidth = (int) (getResources().getDisplayMetrics().widthPixels / 1.2);

        RippleView rippleMenuButton = (RippleView) rootView.findViewById(R.id.chl_ripple_menu_button);
        rippleMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View rippleView) {

                final MaterialDialog dialog = new MaterialDialog.Builder(G.fragmentActivity).customView(R.layout.chat_popup_dialog_custom, true).build();
                View v = dialog.getCustomView();

                DialogAnimation.animationUp(dialog);
                dialog.show();

                ViewGroup root1 = (ViewGroup) v.findViewById(R.id.dialog_root_item1_notification);
                ViewGroup root2 = (ViewGroup) v.findViewById(R.id.dialog_root_item2_notification);
                ViewGroup root3 = (ViewGroup) v.findViewById(R.id.dialog_root_item3_notification);
                ViewGroup root4 = (ViewGroup) v.findViewById(R.id.dialog_root_item4_notification);
                ViewGroup root5 = (ViewGroup) v.findViewById(R.id.dialog_root_item5_notification);
                ViewGroup root6 = (ViewGroup) v.findViewById(R.id.dialog_root_item6_notification);

                TextView txtSearch = (TextView) v.findViewById(R.id.dialog_text_item1_notification);
                TextView txtClearHistory = (TextView) v.findViewById(R.id.dialog_text_item2_notification);
                TextView txtDeleteChat = (TextView) v.findViewById(R.id.dialog_text_item3_notification);
                TextView txtMuteNotification = (TextView) v.findViewById(R.id.dialog_text_item4_notification);
                TextView txtChatToGroup = (TextView) v.findViewById(R.id.dialog_text_item5_notification);
                TextView txtCleanUp = (TextView) v.findViewById(R.id.dialog_text_item6_notification);

                TextView iconSearch = (TextView) v.findViewById(R.id.dialog_icon_item1_notification);
                iconSearch.setText(G.context.getResources().getString(R.string.md_searching_magnifying_glass));

                TextView iconClearHistory = (TextView) v.findViewById(R.id.dialog_icon_item2_notification);
                iconClearHistory.setText(G.context.getResources().getString(R.string.md_clearHistory));

                TextView iconDeleteChat = (TextView) v.findViewById(R.id.dialog_icon_item3_notification);
                iconDeleteChat.setText(G.context.getResources().getString(R.string.md_rubbish_delete_file));

                TextView iconMuteNotification = (TextView) v.findViewById(R.id.dialog_icon_item4_notification);

                TextView iconChatToGroup = (TextView) v.findViewById(R.id.dialog_icon_item5_notification);
                iconChatToGroup.setText(G.context.getResources().getString(R.string.md_users_social_symbol));

                TextView iconCleanUp = (TextView) v.findViewById(R.id.dialog_icon_item6_notification);
                iconCleanUp.setText(G.context.getResources().getString(R.string.md_clean_up));

                root1.setVisibility(View.VISIBLE);
                root2.setVisibility(View.VISIBLE);
                root3.setVisibility(View.VISIBLE);
                root4.setVisibility(View.VISIBLE);
                root5.setVisibility(View.VISIBLE);
                root6.setVisibility(View.VISIBLE);

                txtSearch.setText(G.context.getResources().getString(R.string.Search));
                txtClearHistory.setText(G.context.getResources().getString(R.string.clear_history));
                txtDeleteChat.setText(G.context.getResources().getString(R.string.delete_chat));
                txtMuteNotification.setText(G.context.getResources().getString(R.string.mute_notification));
                txtChatToGroup.setText(G.context.getResources().getString(R.string.chat_to_group));
                txtCleanUp.setText(G.context.getResources().getString(R.string.clean_up));

                if (chatType == CHAT) {
                    root3.setVisibility(View.VISIBLE);
                    if (!isChatReadOnly && !blockUser) {
                        root5.setVisibility(View.VISIBLE);
                    } else {
                        root5.setVisibility(View.GONE);
                    }
                } else {
                    root3.setVisibility(View.GONE);
                    root5.setVisibility(View.GONE);

                    if (chatType == CHANNEL) {
                        root2.setVisibility(View.GONE);
                    }
                }

                //+Realm realm = Realm.getDefaultInstance();
                RealmRoom realmRoom = getRealmChat().where(RealmRoom.class).equalTo(RealmRoomFields.ID, mRoomId).findFirst();
                if (realmRoom != null) {

                    if (realmRoom.getMute()) {
                        txtMuteNotification.setText(G.context.getResources().getString(R.string.unmute_notification));
                        iconMuteNotification.setText(G.context.getResources().getString(R.string.md_unMuted));
                    } else {
                        txtMuteNotification.setText(G.context.getResources().getString(R.string.mute_notification));
                        iconMuteNotification.setText(G.context.getResources().getString(R.string.md_muted));
                    }
                } else {
                    root2.setVisibility(View.GONE);
                    root3.setVisibility(View.GONE);
                    root4.setVisibility(View.GONE);
                    root5.setVisibility(View.GONE);
                    root6.setVisibility(View.GONE);
                }
                //realm.close();

                root1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        initLayoutSearchNavigation();

                        rootView.findViewById(R.id.toolbarContainer).setVisibility(View.GONE);
                        ll_Search.setVisibility(View.VISIBLE);
                        // ll_navigate_Message.setVisibility(View.VISIBLE);
                        //  viewAttachFile.setVisibility(View.GONE);

                        if (!initHash) {
                            initHash = true;
                            initHashView();
                        }

                        edtSearchMessage.requestFocus();
                    }
                });
                root2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();

                        new MaterialDialog.Builder(G.fragmentActivity).title(R.string.clear_history).content(R.string.clear_history_content).positiveText(R.string.yes).onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                onSelectRoomMenu("txtClearHistory", (int) mRoomId);
                            }
                        }).negativeText(R.string.no).show();
                    }
                });
                root3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        new MaterialDialog.Builder(G.fragmentActivity).title(R.string.delete_chat).content(R.string.delete_chat_content).positiveText(R.string.yes).onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                onSelectRoomMenu("txtDeleteChat", (int) mRoomId);
                            }
                        }).negativeText(R.string.no).show();
                    }
                });
                root4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        onSelectRoomMenu("txtMuteNotification", (int) mRoomId);
                    }
                });
                root5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        new MaterialDialog.Builder(G.fragmentActivity).title(R.string.convert_chat_to_group_title).content(R.string.convert_chat_to_group_content).positiveText(R.string.yes).negativeText(R.string.no).onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                //finish();
                                finishChat();
                                dialog.dismiss();
                                G.onConvertToGroup.openFragmentOnActivity("ConvertToGroup", mRoomId);
                            }
                        }).show();
                    }
                });

                root6.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        RealmRoomMessage.ClearAllMessage(getRealmChat(), false, mRoomId);
                        mAdapter.clear();
                        recyclerView.removeAllViews();

                        llScrollNavigate.setVisibility(View.GONE);

                        recyclerView.addOnScrollListener(scrollListener);

                        saveMessageIdPositionState(0);
                        /**
                         * get history from server
                         */
                        resetMessagingValue();
                        topMore = true;
                        getOnlineMessage(0, UP);
                    }
                });
            }
        });

        imvSmileButton = (MaterialDesignTextView) rootView.findViewById(R.id.chl_imv_smile_button);

        edtChat = (EmojiEditTextE) rootView.findViewById(R.id.chl_edt_chat);
        edtChat.requestFocus();

        edtChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEmojiSHow) {

                    imvSmileButton.performClick();
                }
            }
        });

        imvSendButton = (MaterialDesignTextView) rootView.findViewById(R.id.chl_imv_send_button);
        imvSendButton.setTextColor(Color.parseColor(G.attachmentColor));

        imvAttachFileButton = (MaterialDesignTextView) rootView.findViewById(R.id.chl_imv_attach_button);
        layoutAttachBottom = (LinearLayout) rootView.findViewById(R.id.layoutAttachBottom);

        imvMicButton = (MaterialDesignTextView) rootView.findViewById(R.id.chl_imv_mic_button);

        mAdapter = new MessagesAdapter<>(this, this, this);

        mAdapter.getItemFilter().withFilterPredicate(new IItemAdapter.Predicate<AbstractMessage>() {
            @Override
            public boolean filter(AbstractMessage item, CharSequence constraint) {
                return !item.mMessage.messageText.toLowerCase().contains(constraint.toString().toLowerCase());
            }
        });

        //FragmentMain.PreCachingLayoutManager layoutManager = new FragmentMain.PreCachingLayoutManager(ActivityChat.this, 7500);
        MyLinearLayoutManager layoutManager = new MyLinearLayoutManager(G.fragmentActivity);
        layoutManager.setStackFromEnd(true);

        if (recyclerView == null) {
            recyclerView = (RecyclerView) rootView.findViewById(R.id.chl_recycler_view_chat);
        }

        recyclerView.setItemAnimator(null);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter);

        /**
         * load message , use handler for load async
         */

        if (mAdapter.getItemCount() > 0) {
            txtEmptyMessages.setVisibility(View.GONE);
        } else {
            txtEmptyMessages.setVisibility(View.VISIBLE);
        }

        mAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {

            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                if (mAdapter.getItemCount() > 0) {
                    txtEmptyMessages.setVisibility(View.GONE);
                } else {
                    txtEmptyMessages.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                super.onItemRangeRemoved(positionStart, itemCount);
                if (mAdapter.getItemCount() > 0) {
                    txtEmptyMessages.setVisibility(View.GONE);
                } else {
                    txtEmptyMessages.setVisibility(View.VISIBLE);
                }
            }
        });

        llScrollNavigate = (FrameLayout) rootView.findViewById(R.id.ac_ll_scrool_navigate);
        txtNewUnreadMessage = (TextView) rootView.findViewById(R.id.cs_txt_unread_message);

        G.handler.post(new Runnable() {
            @Override
            public void run() {
                getMessages();
                manageForwardedMessage();
            }
        });

        AndroidUtils.setBackgroundShapeColor(txtNewUnreadMessage, Color.parseColor(G.notificationColor));

        MaterialDesignTextView txtNavigationLayout = (MaterialDesignTextView) rootView.findViewById(R.id.ac_txt_down_navigation);
        AndroidUtils.setBackgroundShapeColor(txtNavigationLayout, Color.parseColor(G.appBarColor));

        llScrollNavigate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                latestButtonClickTime = System.currentTimeMillis();
                /**
                 * have unread
                 */
                if (countNewMessage > 0 && firstUnreadMessageInChat != null) {
                    /**
                     * if unread message is exist in list set position to this item and create
                     * unread layout otherwise should clear list and load from unread again
                     */

                    firstUnreadMessage = firstUnreadMessageInChat;
                    if (!firstUnreadMessage.isValid() || firstUnreadMessage.isDeleted()) {
                        resetAndGetFromEnd();
                        return;
                    }

                    int position = mAdapter.findPositionByMessageId(firstUnreadMessage.getMessageId());
                    if (position > 0) {

                        RealmRoomMessage unreadMessage = new RealmRoomMessage();
                        unreadMessage.setMessageId(TimeUtils.currentLocalTime());
                        // -1 means time message
                        unreadMessage.setUserId(-1);
                        unreadMessage.setMessage(countNewMessage + " " + G.context.getResources().getString(R.string.unread_message));
                        unreadMessage.setMessageType(ProtoGlobal.RoomMessageType.TEXT);
                        mAdapter.add(position, new UnreadMessage(getRealmChat(), FragmentChat.this).setMessage(StructMessageInfo.convert(getRealmChat(), unreadMessage)).withIdentifier(SUID.id().get()));

                        LinearLayoutManager linearLayout = (LinearLayoutManager) recyclerView.getLayoutManager();
                        linearLayout.scrollToPositionWithOffset(position, 0);
                    } else {
                        resetMessagingValue();
                        unreadCount = countNewMessage;
                        firstUnreadMessage = firstUnreadMessageInChat;
                        getMessages();

                        if (firstUnreadMessage == null) {
                            resetAndGetFromEnd();
                            return;
                        }

                        int position1 = mAdapter.findPositionByMessageId(firstUnreadMessage.getMessageId());
                        if (position1 > 0) {
                            LinearLayoutManager linearLayout = (LinearLayoutManager) recyclerView.getLayoutManager();
                            linearLayout.scrollToPositionWithOffset(position1 - 1, 0);
                        }
                    }
                    firstUnreadMessageInChat = null;
                    countNewMessage = 0;
                    txtNewUnreadMessage.setVisibility(View.GONE);
                    txtNewUnreadMessage.setText(countNewMessage + "");
                } else {
                    llScrollNavigate.setVisibility(View.GONE);
                    /**
                     * if addToView is true this means that all new message is in adapter
                     * and just need go to end position in list otherwise we should clear all
                     * items and reload again from bottom
                     */
                    if (!addToView) {
                        resetMessagingValue();
                        getMessages();
                    } else {
                        scrollToEnd();
                    }
                }
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                int lastVisiblePosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();

                if (!firsInitScrollPosition) {
                    lastPosition = lastVisiblePosition;
                    firsInitScrollPosition = true;
                }

                int state = lastPosition - lastVisiblePosition;
                if (state > 0) {   // up

                    if (countNewMessage == 0) {
                        llScrollNavigate.setVisibility(View.GONE);
                    } else {
                        llScrollNavigate.setVisibility(View.VISIBLE);

                        txtNewUnreadMessage.setText(countNewMessage + "");
                        txtNewUnreadMessage.setVisibility(View.VISIBLE);
                    }

                    lastPosition = lastVisiblePosition;
                } else if (state < 0) { //down

                    if (mAdapter.getItemCount() - lastVisiblePosition > 10) {
                        /**
                         * show llScrollNavigate if timeout from latest click
                         */
                        if (HelperTimeOut.timeoutChecking(0, latestButtonClickTime, (int) (2 * DateUtils.SECOND_IN_MILLIS))) {
                            llScrollNavigate.setVisibility(View.VISIBLE);
                        }
                        if (countNewMessage > 0) {
                            txtNewUnreadMessage.setText(countNewMessage + "");
                            txtNewUnreadMessage.setVisibility(View.VISIBLE);
                        } else {
                            txtNewUnreadMessage.setVisibility(View.GONE);
                        }
                    } else {
                        /**
                         * if addToView is true means that
                         */
                        if (addToView) {

                            /**
                             * if countNewMessage is bigger than zero in onItemShowingMessageId
                             * callback txtNewUnreadMessage visibility will be managed
                             */
                            if (countNewMessage == 0) {
                                if (mAdapter.getItemCount() - lastVisiblePosition < 10) {
                                    llScrollNavigate.setVisibility(View.GONE);
                                }
                            }
                        }
                    }

                    lastPosition = lastVisiblePosition;
                }
            }
        });

        rippleBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeKeyboard(view);
                popBackStackFragment();
                //finishChat();
            }
        });

        imvUserPicture = (CircleImageView) rootView.findViewById(R.id.chl_imv_user_picture);
        imvUserPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToProfile();
            }
        });

        rootView.findViewById(R.id.ac_txt_cloud).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToProfile();
            }
        });

        lyt_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToProfile();
            }
        });

        imvSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!addToView) {
                    resetAndGetFromEnd();
                }

                //final Realm realmMessage = Realm.getDefaultInstance();

                HelperSetAction.setCancel(mRoomId);

                clearDraftRequest();

                if (hasForward) {
                    manageForwardedMessage();

                    if (edtChat.getText().length() == 0) {
                        return;
                    }
                }

                if (ll_attach_text.getVisibility() == View.VISIBLE) {

                    if (listPathString.size() == 0) {
                        return;
                    }
                    sendMessage(latestRequestCode, listPathString.get(0));
                    listPathString.clear();
                    ll_attach_text.setVisibility(View.GONE);
                    edtChat.setFilters(new InputFilter[]{});
                    edtChat.setText("");

                    clearReplyView();
                    return;
                }

                /**
                 * if use click on edit message, the message's text will be put to the EditText
                 * i set the message object for that view's tag to obtain it here
                 * request message edit only if there is any changes to the message text
                 */

                if (edtChat.getTag() != null && edtChat.getTag() instanceof StructMessageInfo) {
                    final StructMessageInfo messageInfo = (StructMessageInfo) edtChat.getTag();
                    final String message = getWrittenMessage();
                    if (!message.equals(messageInfo.messageText)) {

                        getRealmChat().executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                RealmRoomMessage roomMessage = realm.where(RealmRoomMessage.class).equalTo(RealmRoomMessageFields.MESSAGE_ID, parseLong(messageInfo.messageID)).findFirst();

                                RealmClientCondition realmClientCondition = realm.where(RealmClientCondition.class).equalTo(RealmClientConditionFields.ROOM_ID, mRoomId).findFirst();

                                RealmOfflineEdited realmOfflineEdited = realm.createObject(RealmOfflineEdited.class, SUID.id().get());
                                realmOfflineEdited.setMessageId(parseLong(messageInfo.messageID));
                                realmOfflineEdited.setMessage(message);
                                realmOfflineEdited = realm.copyToRealm(realmOfflineEdited);

                                realmClientCondition.getOfflineEdited().add(realmOfflineEdited);

                                if (roomMessage != null) {
                                    // update message text in database
                                    roomMessage.setMessage(message);
                                    roomMessage.setEdited(true);
                                    RealmRoomMessage.addTimeIfNeed(roomMessage, realm);
                                    RealmRoomMessage.isEmojiInText(roomMessage, message);

                                    messageInfo.hasEmojiInText = roomMessage.isHasEmojiInText();
                                }

                                RealmRoom rm = realm.where(RealmRoom.class).equalTo(RealmRoomFields.ID, mRoomId).findFirst();
                                if (rm != null) {
                                    rm.setUpdatedTime(TimeUtils.currentLocalTime());
                                }
                            }
                        });

                        //End

                        // I'm in the room
                        G.handler.post(new Runnable() {
                            @Override
                            public void run() {
                                // update message text in adapter
                                mAdapter.updateMessageText(parseLong(messageInfo.messageID), message);
                            }
                        });

                        /**
                         * should be null after requesting
                         */
                        edtChat.setTag(null);
                        clearReplyView();
                        edtChat.setText("");

                        /**
                         * send edit message request
                         */
                        if (chatType == CHAT) {
                            new RequestChatEditMessage().chatEditMessage(mRoomId, parseLong(messageInfo.messageID), message);
                        } else if (chatType == GROUP) {
                            new RequestGroupEditMessage().groupEditMessage(mRoomId, parseLong(messageInfo.messageID), message);
                        } else if (chatType == CHANNEL) {
                            new RequestChannelEditMessage().channelEditMessage(mRoomId, parseLong(messageInfo.messageID), message);
                        }
                    }
                } else { // new message has written

                    final long senderId = G.userId;

                    String[] messages = HelperString.splitStringEvery(getWrittenMessage(), Config.MAX_TEXT_LENGTH);
                    if (messages.length == 0) {
                        edtChat.setText("");
                        Toast.makeText(context, R.string.please_write_your_message, Toast.LENGTH_LONG).show();
                    } else {
                        for (int i = 0; i < messages.length; i++) {
                            final String message = messages[i];
                            if (!message.isEmpty()) {
                                final RealmRoom room = getRealmChat().where(RealmRoom.class).equalTo(RealmRoomFields.ID, mRoomId).findFirst();
                                final String identity = Long.toString(SUID.id().get());
                                final long currentTime = TimeUtils.currentLocalTime();

                                getRealmChat().executeTransaction(new Realm.Transaction() {
                                    @Override
                                    public void execute(Realm realm) {
                                        RealmRoomMessage roomMessage = realm.createObject(RealmRoomMessage.class, parseLong(identity));

                                        roomMessage.setMessageType(ProtoGlobal.RoomMessageType.TEXT);
                                        roomMessage.setMessage(message);
                                        roomMessage.setStatus(ProtoGlobal.RoomMessageStatus.SENDING.toString());

                                        RealmRoomMessage.addTimeIfNeed(roomMessage, realm);
                                        RealmRoomMessage.isEmojiInText(roomMessage, message);

                                        roomMessage.setRoomId(mRoomId);
                                        roomMessage.setShowMessage(true);

                                        roomMessage.setUserId(senderId);
                                        roomMessage.setAuthorHash(G.authorHash);
                                        roomMessage.setCreateTime(currentTime);

                                        /**
                                         *  user wants to replay to a message
                                         */
                                        if (userTriesReplay()) {
                                            RealmRoomMessage messageToReplay = realm.where(RealmRoomMessage.class).equalTo(RealmRoomMessageFields.MESSAGE_ID, parseLong(((StructMessageInfo) mReplayLayout.getTag()).messageID)).findFirst();
                                            if (messageToReplay != null) {
                                                roomMessage.setReplyTo(messageToReplay);
                                            }
                                        }
                                    }
                                });

                                final RealmRoomMessage roomMessage = getRealmChat().where(RealmRoomMessage.class).equalTo(RealmRoomMessageFields.MESSAGE_ID, parseLong(identity)).findFirst();

                                if (roomMessage != null) {
                                    getRealmChat().executeTransaction(new Realm.Transaction() {
                                        @Override
                                        public void execute(Realm realm) {
                                            if (room != null) {
                                                room.setLastMessage(roomMessage);
                                            }
                                        }
                                    });
                                }

                                if (chatType == CHANNEL) {
                                    getRealmChat().executeTransaction(new Realm.Transaction() {
                                        @Override
                                        public void execute(Realm realm) {
                                            RealmChannelExtra realmChannelExtra = realm.createObject(RealmChannelExtra.class);
                                            realmChannelExtra.setMessageId(parseLong(identity));
                                            realmChannelExtra.setThumbsUp("0");
                                            realmChannelExtra.setThumbsDown("0");
                                            if (realmRoom != null && realmRoom.getChannelRoom() != null && realmRoom.getChannelRoom().isSignature()) {
                                                realmChannelExtra.setSignature(G.displayName);
                                            } else {
                                                realmChannelExtra.setSignature("");
                                            }
                                            realmChannelExtra.setViewsLabel("1");
                                        }
                                    });
                                }
                                mAdapter.add(new TextItem(getRealmChat(), chatType, FragmentChat.this).setMessage(StructMessageInfo.convert(getRealmChat(), roomMessage)).withIdentifier(SUID.id().get()));

                                scrollToEnd();

                                /**
                                 * send splitted message in every one second
                                 */
                                if (messages.length > 1) {
                                    G.handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (roomMessage != null && roomMessage.isValid() && !roomMessage.isDeleted()) {
                                                new ChatSendMessageUtil().build(chatType, mRoomId, roomMessage);
                                            }
                                        }
                                    }, 1000 * i);
                                } else {
                                    new ChatSendMessageUtil().build(chatType, mRoomId, roomMessage);
                                }

                                edtChat.setText("");

                                /**
                                 * if replay layout is visible, gone it
                                 */
                                clearReplyView();
                            } else {
                                Toast.makeText(context, R.string.please_write_your_message, Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }

                //realmMessage.close();
            }
        });

        imvAttachFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!initAttach) {
                    initAttach = true;
                    initAttach();
                }

                InputMethodManager imm = (InputMethodManager) G.fragmentActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                itemAdapterBottomSheet();
            }
        });

        imvMicButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                if (ContextCompat.checkSelfPermission(G.fragmentActivity, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                    try {
                        HelperPermision.getMicroPhonePermission(G.fragmentActivity, null);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    if (ContextCompat.checkSelfPermission(G.fragmentActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        try {
                            HelperPermision.getStoragePermision(G.fragmentActivity, null);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        voiceRecord.setItemTag("ivVoice");
                        // viewAttachFile.setVisibility(View.GONE);
                        viewMicRecorder.setVisibility(View.VISIBLE);
                        voiceRecord.startVoiceRecord();
                    }
                }

                return true;
            }
        });

        // to toggle between keyboard and emoji popup
        imvSmileButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!initEmoji) {
                    initEmoji = true;
                    setUpEmojiPopup();
                }

                emojiPopup.toggle();
            }
        });

        edtChat.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence text, int i, int i1, int i2) {

                if (text.length() > 0) {
                    HelperSetAction.setActionTyping(mRoomId, chatType);
                }

                // if in the seeting page send by enter is on message send by enter key
                if (text.toString().endsWith(System.getProperty("line.separator"))) {
                    if (sendByEnter) imvSendButton.performClick();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (ll_attach_text.getVisibility() == View.GONE && hasForward == false) {

                    if (edtChat.getText().length() > 0) {
                        layoutAttachBottom.animate().alpha(0F).setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                layoutAttachBottom.setVisibility(View.GONE);
                            }
                        }).start();
                        imvSendButton.animate().alpha(1F).setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                G.handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        imvSendButton.clearAnimation();
                                        imvSendButton.setVisibility(View.VISIBLE);
                                    }
                                });
                            }
                        }).start();
                    } else {
                        layoutAttachBottom.animate().alpha(1F).setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);

                                layoutAttachBottom.setVisibility(View.VISIBLE);
                            }
                        }).start();
                        imvSendButton.animate().alpha(0F).setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                G.handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        imvSendButton.clearAnimation();
                                        imvSendButton.setVisibility(View.GONE);
                                    }
                                });
                            }
                        }).start();
                    }
                }
            }
        });

        //realm.close();
    }

    private void putExtra(Intent intent, StructMessageInfo messageInfo) {
        try {
            String filePath = messageInfo.forwardedFrom != null ? messageInfo.forwardedFrom.getAttachment().getLocalFilePath() : messageInfo.attachment.getLocalFilePath();

            if (filePath != null) {
                intent.putExtra(Intent.EXTRA_STREAM, AppUtils.createtUri(new File(filePath)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * *************************** callbacks ***************************
     */

    @Override
    public void onSenderAvatarClick(View view, StructMessageInfo messageInfo, int position) {
        /**
         * set null for avoid from clear group room message adapter if user try for clearChatHistory
         */
        G.onClearChatHistory = null;

        new HelperFragment(FragmentContactsProfile.newInstance(mRoomId, parseLong(messageInfo.senderID), GROUP.toString())).setReplace(false).load();
    }

    @Override
    public void onUploadOrCompressCancel(View view, final StructMessageInfo message, int pos, SendingStep sendingStep) {

        if (sendingStep == SendingStep.UPLOADING) {
            HelperSetAction.sendCancel(parseLong(message.messageID));

            if (HelperUploadFile.cancelUploading(message.messageID)) {
                clearItem(parseLong(message.messageID), pos);
            }
        } else if (sendingStep == SendingStep.COMPRESSING) {

            /**
             * clear path for avoid from continue uploading after compressed file
             */
            for (StructUploadVideo structUploadVideo : structUploadVideos) {
                if (structUploadVideo.filePath.equals(message.attachment.getLocalFilePath())) {
                    structUploadVideo.filePath = "";
                }
            }
            clearItem(parseLong(message.messageID), pos);
        }
    }

    @Override
    public void onChatClearMessage(final long roomId, final long clearId, ProtoResponse.Response response) {
        G.handler.post(new Runnable() {
            @Override
            public void run() {
                if (mAdapter != null) {
                    mAdapter.clear();
                }
                if (recyclerView != null) {
                    recyclerView.removeAllViews();
                }

                /**
                 * remove tag from edtChat if the message has deleted
                 */
                if (edtChat.getTag() != null && edtChat.getTag() instanceof StructMessageInfo) {
                    edtChat.setTag(null);
                }
            }
        });
    }

    @Override
    public void onChatUpdateStatus(long roomId, final long messageId, final ProtoGlobal.RoomMessageStatus status, long statusVersion) {

        // I'm in the room
        if (mRoomId == roomId) {
            G.handler.post(new Runnable() {
                @Override
                public void run() {
                    if (mAdapter != null) {
                        mAdapter.updateMessageStatus(messageId, status);
                    }
                }
            });
        }
    }

    @Override
    public void onChatMessageSelectionChanged(int selectedCount, Set<AbstractMessage> selectedItems) {
        //   Toast.makeText(ActivityChat.this, "selected: " + Integer.toString(selectedCount), Toast.LENGTH_SHORT).show();
        if (selectedCount > 0) {
            toolbar.setVisibility(View.GONE);
            rippleReplaySelected.setVisibility(View.VISIBLE);

            txtNumberOfSelected.setText(selectedCount + "");

            if (HelperCalander.isLanguagePersian) {
                txtNumberOfSelected.setText(HelperCalander.convertToUnicodeFarsiNumber(txtNumberOfSelected.getText().toString()));
            }

            if (selectedCount > 1) {
                rippleReplaySelected.setVisibility(View.INVISIBLE);
            } else {

                if (chatType == CHANNEL) {
                    if (channelRole == ChannelChatRole.MEMBER) {
                        rippleReplaySelected.setVisibility(View.INVISIBLE);
                    }
                }
            }

            //+Realm realm = Realm.getDefaultInstance();

            for (AbstractMessage message : selectedItems) {

                RealmRoom realmRoom = getRealmChat().where(RealmRoom.class).equalTo(RealmRoomFields.ID, mRoomId).findFirst();
                if (realmRoom != null) {

                    long messageSender = 0;
                    if (message != null && message.mMessage != null && message.mMessage.senderID != null) {
                        messageSender = parseLong(message.mMessage.senderID);
                    } else {
                        continue;
                    }

                    // if user clicked on any message which he wasn't its sender, remove edit mList option
                    if (chatType == CHANNEL) {
                        if (channelRole == ChannelChatRole.MEMBER) {
                            rippleReplaySelected.setVisibility(View.INVISIBLE);
                            rippleDeleteSelected.setVisibility(View.GONE);
                        }
                        final long senderId = G.userId;
                        ChannelChatRole roleSenderMessage = null;
                        RealmChannelRoom realmChannelRoom = realmRoom.getChannelRoom();
                        RealmList<RealmMember> realmMembers = realmChannelRoom.getMembers();
                        for (RealmMember rm : realmMembers) {
                            if (rm.getPeerId() == messageSender) {
                                roleSenderMessage = ChannelChatRole.valueOf(rm.getRole());
                            }
                        }
                        if (senderId != messageSender) {  // if message dose'nt belong to owner
                            if (channelRole == ChannelChatRole.MEMBER) {
                                rippleDeleteSelected.setVisibility(View.GONE);
                            } else if (channelRole == ChannelChatRole.MODERATOR) {
                                if (roleSenderMessage == ChannelChatRole.MODERATOR || roleSenderMessage == ChannelChatRole.ADMIN || roleSenderMessage == ChannelChatRole.OWNER) {
                                    rippleDeleteSelected.setVisibility(View.GONE);
                                }
                            } else if (channelRole == ChannelChatRole.ADMIN) {
                                if (roleSenderMessage == ChannelChatRole.OWNER || roleSenderMessage == ChannelChatRole.ADMIN) {
                                    rippleDeleteSelected.setVisibility(View.GONE);
                                }
                            }
                        } else {
                            rippleDeleteSelected.setVisibility(View.VISIBLE);
                        }
                    } else if (chatType == GROUP) {

                        final long senderId = G.userId;

                        GroupChatRole roleSenderMessage = null;
                        RealmGroupRoom realmGroupRoom = realmRoom.getGroupRoom();
                        RealmList<RealmMember> realmMembers = realmGroupRoom.getMembers();
                        for (RealmMember rm : realmMembers) {
                            if (rm.getPeerId() == messageSender) {
                                roleSenderMessage = GroupChatRole.valueOf(rm.getRole());
                            }
                        }
                        if (senderId != messageSender) {  // if message dose'nt belong to owner
                            if (groupRole == GroupChatRole.MEMBER) {
                                rippleDeleteSelected.setVisibility(View.GONE);
                            } else if (groupRole == GroupChatRole.MODERATOR) {
                                if (roleSenderMessage == GroupChatRole.MODERATOR || roleSenderMessage == GroupChatRole.ADMIN || roleSenderMessage == GroupChatRole.OWNER) {
                                    rippleDeleteSelected.setVisibility(View.GONE);
                                }
                            } else if (groupRole == GroupChatRole.ADMIN) {
                                if (roleSenderMessage == GroupChatRole.OWNER || roleSenderMessage == GroupChatRole.ADMIN) {
                                    rippleDeleteSelected.setVisibility(View.GONE);
                                }
                            }
                        } else {
                            rippleDeleteSelected.setVisibility(View.VISIBLE);
                        }
                    } else if (realmRoom.getReadOnly()) {
                        rippleReplaySelected.setVisibility(View.INVISIBLE);
                    }
                }
            }

            //realm.close();

            ll_AppBarSelected.setVisibility(View.VISIBLE);
        } else {
            toolbar.setVisibility(View.VISIBLE);
            ll_AppBarSelected.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPreChatMessageRemove(final StructMessageInfo messageInfo, int position) {
        if (mAdapter.getAdapterItemCount() > 1 && position == mAdapter.getAdapterItemCount() - 1) {
            // if was last message removed
            // update room last message
            //Realm realm = Realm.getDefaultInstance();

            getRealmChat().executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    try {
                        RealmRoom realmRoom = realm.where(RealmRoom.class).equalTo(RealmRoomFields.ID, mRoomId).findFirst();
                        long _deletedMessageId = parseLong(messageInfo.messageID);
                        RealmRoomMessage realmRoomMessage = null;
                        RealmResults<RealmRoomMessage> realmRoomMessages = realm.where(RealmRoomMessage.class).equalTo(RealmRoomMessageFields.EDITED, false).equalTo(RealmRoomMessageFields.ROOM_ID, mRoomId).lessThan(RealmRoomMessageFields.MESSAGE_ID, _deletedMessageId).findAll();
                        if (realmRoomMessages.size() > 0) {
                            realmRoomMessage = realmRoomMessages.last();
                        }

                        if (realmRoom != null && realmRoomMessage != null) {
                            realmRoom.setLastMessage(realmRoomMessage);
                        }
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                }
            });

            //realm.close();
        }
    }

    @Override
    public void onMessageUpdate(long roomId, final long messageId, final ProtoGlobal.RoomMessageStatus status, final String identity, ProtoGlobal.RoomMessage roomMessage) {
        // I'm in the room
        if (roomId == mRoomId && mAdapter != null) {
            G.handler.post(new Runnable() {
                @Override
                public void run() {
                    mAdapter.updateMessageIdAndStatus(messageId, identity, status);
                }
            });
        }
    }

    @Override
    public void onMessageReceive(final long roomId, String message, ProtoGlobal.RoomMessageType messageType, final ProtoGlobal.RoomMessage roomMessage, final ProtoGlobal.Room.Type roomType) {

        if (roomMessage.getMessageId() <= biggestMessageId) {
            return;
        }

        G.handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //final Realm realm = Realm.getDefaultInstance();
                final RealmRoomMessage realmRoomMessage = getRealmChat().where(RealmRoomMessage.class).equalTo(RealmRoomMessageFields.MESSAGE_ID, roomMessage.getMessageId()).findFirst();

                if (realmRoomMessage != null && realmRoomMessage.isValid()) {
                    if (roomMessage.getAuthor().getUser() != null) {
                        if (roomMessage.getAuthor().getUser().getUserId() != G.userId) {
                            // I'm in the room
                            if (roomId == mRoomId) {
                                // I'm in the room, so unread messages count is 0. it means, I read all messages
                                getRealmChat().executeTransaction(new Realm.Transaction() {
                                    @Override
                                    public void execute(Realm realm) {
                                        RealmRoom room = realm.where(RealmRoom.class).equalTo(RealmRoomFields.ID, mRoomId).findFirst();
                                        if (room != null) {
                                            /**
                                             * client checked  (room.getUnreadCount() <= 1)  because in HelperMessageResponse unreadCount++
                                             */
                                            if (room.getUnreadCount() <= 1 && countNewMessage < 1) {
                                                firstUnreadMessage = realmRoomMessage;
                                            }
                                            room.setUnreadCount(0);
                                        }
                                    }
                                });

                                /**
                                 * when user receive message, I send update status as SENT to the message sender
                                 * but imagine user is not in the room (or he is in another room) and received
                                 * some messages when came back to the room with new messages, I make new update
                                 * status request as SEEN to the message sender
                                 */

                                //Start ClientCondition OfflineSeen
                                getRealmChat().executeTransaction(new Realm.Transaction() {
                                    @Override
                                    public void execute(Realm realm) {
                                        final RealmClientCondition realmClientCondition = realm.where(RealmClientCondition.class).equalTo(RealmClientConditionFields.ROOM_ID, mRoomId).findFirst();
                                        if (!isNotJoin) {
                                            // make update status to message sender that i've read his message

                                            StructBackGroundSeen _BackGroundSeen = null;

                                            ProtoGlobal.RoomMessageStatus roomMessageStatus;
                                            if (G.isAppInFg && isEnd()) {

                                                /**
                                                 * I'm in the room, so unread messages count is 0. it means, I read all messages
                                                 */
                                                RealmRoom room = realm.where(RealmRoom.class).equalTo(RealmRoomFields.ID, mRoomId).findFirst();
                                                if (room != null) {
                                                    room.setUnreadCount(0);
                                                }

                                                if (realmClientCondition != null) {
                                                    if (realmRoomMessage.isValid() && !realmRoomMessage.getStatus().equalsIgnoreCase(ProtoGlobal.RoomMessageStatus.SEEN.toString())) {
                                                        realmRoomMessage.setStatus(ProtoGlobal.RoomMessageStatus.SEEN.toString());

                                                        RealmOfflineSeen realmOfflineSeen = realm.createObject(RealmOfflineSeen.class, SUID.id().get());
                                                        realmOfflineSeen.setOfflineSeen(realmRoomMessage.getMessageId());
                                                        realm.copyToRealmOrUpdate(realmOfflineSeen);
                                                        if (realmClientCondition.getOfflineSeen() != null) {
                                                            realmClientCondition.getOfflineSeen().add(realmOfflineSeen);
                                                        } else {
                                                            RealmList<RealmOfflineSeen> offlineSeenList = new RealmList<>();
                                                            offlineSeenList.add(realmOfflineSeen);
                                                            realmClientCondition.setOfflineSeen(offlineSeenList);
                                                        }
                                                    }
                                                }

                                                roomMessageStatus = ProtoGlobal.RoomMessageStatus.SEEN;
                                            } else {

                                                roomMessageStatus = ProtoGlobal.RoomMessageStatus.DELIVERED;

                                                _BackGroundSeen = new StructBackGroundSeen();
                                                _BackGroundSeen.messageID = roomMessage.getMessageId();
                                                _BackGroundSeen.roomType = roomType;
                                            }

                                            if (chatType == CHAT) {
                                                G.chatUpdateStatusUtil.sendUpdateStatus(roomType, roomId, roomMessage.getMessageId(), roomMessageStatus);

                                                if (_BackGroundSeen != null) {
                                                    backGroundSeenList.add(_BackGroundSeen);
                                                }
                                            } else if (chatType == GROUP && (roomMessage.getStatus() != ProtoGlobal.RoomMessageStatus.SEEN)) {
                                                G.chatUpdateStatusUtil.sendUpdateStatus(roomType, roomId, roomMessage.getMessageId(), roomMessageStatus);

                                                if (_BackGroundSeen != null) {
                                                    backGroundSeenList.add(_BackGroundSeen);
                                                }
                                            }
                                        }
                                    }
                                });

                                /**
                                 * when client load item from unread and don't come down let's not add the message
                                 * to the list and after insuring that not any more message in DOWN can add message
                                 */
                                if (addToView) {
                                    switchAddItem(new ArrayList<>(Collections.singletonList(StructMessageInfo.convert(getRealmChat(), realmRoomMessage))), false);
                                }

                                setBtnDownVisible(realmRoomMessage);
                            } else {
                                if (!isNotJoin) {
                                    // user has received the message, so I make a new delivered update status request
                                    if (roomType == CHAT) {
                                        G.chatUpdateStatusUtil.sendUpdateStatus(roomType, roomId, roomMessage.getMessageId(), ProtoGlobal.RoomMessageStatus.DELIVERED);
                                    } else if (roomType == GROUP && roomMessage.getStatus() == ProtoGlobal.RoomMessageStatus.SENT) {
                                        G.chatUpdateStatusUtil.sendUpdateStatus(roomType, roomId, roomMessage.getMessageId(), ProtoGlobal.RoomMessageStatus.DELIVERED);
                                    }
                                }
                            }
                        } else {

                            if (roomId == mRoomId) {
                                // I'm sender . but another account sent this message and i received it.
                                if (addToView) {
                                    switchAddItem(new ArrayList<>(Collections.singletonList(StructMessageInfo.convert(getRealmChat(), realmRoomMessage))), false);
                                }
                                setBtnDownVisible(realmRoomMessage);
                            }
                        }
                    }
                }

                //realm.close();
            }
        }, 400);
    }

    @Override
    public void onMessageFailed(long roomId, RealmRoomMessage message) {

        if (mAdapter != null && message != null && roomId == mRoomId) {
            mAdapter.updateMessageStatus(message.getMessageId(), ProtoGlobal.RoomMessageStatus.FAILED);
        }
    }

    @Override
    public void onVoiceRecordDone(final String savedPath) {
        sendCancelAction();

        //+Realm realm = Realm.getDefaultInstance();
        final long messageId = SUID.id().get();
        final long updateTime = TimeUtils.currentLocalTime();
        final long senderID = G.userId;
        final long duration = AndroidUtils.getAudioDuration(G.fragmentActivity, savedPath) / 1000;

        getRealmChat().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmRoomMessage roomMessage = realm.createObject(RealmRoomMessage.class, messageId);
                roomMessage.setMessageType(ProtoGlobal.RoomMessageType.VOICE);
                roomMessage.setMessage(getWrittenMessage());
                roomMessage.setRoomId(mRoomId);
                roomMessage.setStatus(ProtoGlobal.RoomMessageStatus.SENDING.toString());
                roomMessage.setAttachment(messageId, savedPath, 0, 0, 0, null, duration, LocalFileType.FILE);
                roomMessage.setUserId(senderID);
                roomMessage.setCreateTime(updateTime);
                voiceLastMessage = roomMessage;

                // in response to a message as replay, so after server done creating replay and
                // forward options, modify this section and sending message as well.
            }
        });

        StructMessageInfo messageInfo;

        if (userTriesReplay()) {
            messageInfo = new StructMessageInfo(getRealmChat(), mRoomId, Long.toString(messageId), Long.toString(senderID), ProtoGlobal.RoomMessageStatus.SENDING.toString(), ProtoGlobal.
                    RoomMessageType.VOICE, MyType.SendType.send, null, savedPath, updateTime, parseLong(((StructMessageInfo) mReplayLayout.getTag()).messageID));
        } else {
            if (isMessageWrote()) {
                messageInfo = new StructMessageInfo(getRealmChat(), mRoomId, Long.toString(messageId), Long.toString(senderID), ProtoGlobal.RoomMessageStatus.SENDING.toString(), ProtoGlobal.
                        RoomMessageType.VOICE, MyType.SendType.send, null, savedPath, updateTime);
            } else {
                messageInfo = new StructMessageInfo(getRealmChat(), mRoomId, Long.toString(messageId), Long.toString(senderID), ProtoGlobal.RoomMessageStatus.SENDING.toString(), ProtoGlobal.
                        RoomMessageType.VOICE, MyType.SendType.send, null, savedPath, updateTime);
            }
        }

        HelperUploadFile.startUploadTaskChat(mRoomId, chatType, savedPath, messageId, ProtoGlobal.RoomMessageType.VOICE, getWrittenMessage(), StructMessageInfo.getReplyMessageId(messageInfo), new HelperUploadFile.UpdateListener() {
            @Override
            public void OnProgress(int progress, FileUploadStructure struct) {
                if (canUpdateAfterDownload) {
                    insertItemAndUpdateAfterStartUpload(progress, struct);
                }
            }

            @Override
            public void OnError() {

            }
        });

        messageInfo.attachment.duration = duration;

        StructChannelExtra structChannelExtra = new StructChannelExtra();
        structChannelExtra.messageId = messageId;
        structChannelExtra.thumbsUp = "0";
        structChannelExtra.thumbsDown = "0";
        structChannelExtra.viewsLabel = "1";
        final RealmRoom realmRoom = getRealmChat().where(RealmRoom.class).equalTo(RealmRoomFields.ID, mRoomId).findFirst();
        getRealmChat().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                if (voiceLastMessage != null) {
                    realmRoom.setLastMessage(voiceLastMessage);
                }
            }
        });
        if (realmRoom != null && realmRoom.getChannelRoom() != null && realmRoom.getChannelRoom().isSignature()) {
            structChannelExtra.signature = G.displayName;
        } else {
            structChannelExtra.signature = "";
        }
        messageInfo.channelExtra = structChannelExtra;
        mAdapter.add(new VoiceItem(getRealmChat(), chatType, this).setMessage(messageInfo));
        //realm.close();
        scrollToEnd();
        clearReplyView();
    }

    @Override
    public void onVoiceRecordCancel() {
        //empty

        sendCancelAction();
    }

    @Override
    public void onUserInfo(final ProtoGlobal.RegisteredUser user, String identity) {

        if (isCloudRoom) {
            rootView.findViewById(R.id.ac_txt_cloud).setVisibility(View.VISIBLE);
            imvUserPicture.setVisibility(View.GONE);
        } else {
            setAvatar();
        }
    }

    @Override
    public void onUserInfoTimeOut() {
        //empty
    }

    @Override
    public void onUserInfoError(int majorCode, int minorCode) {
        //empty
    }

    @Override
    public void onOpenClick(View view, StructMessageInfo message, int pos) {
        ProtoGlobal.RoomMessageType messageType = message.forwardedFrom != null ? message.forwardedFrom.getMessageType() : message.messageType;
        //+Realm realm = Realm.getDefaultInstance();
        if (messageType == ProtoGlobal.RoomMessageType.IMAGE || messageType == IMAGE_TEXT || messageType == VIDEO || messageType == VIDEO_TEXT) {
            showImage(message, view);
        } else if (messageType == ProtoGlobal.RoomMessageType.FILE || messageType == ProtoGlobal.RoomMessageType.FILE_TEXT) {

            String _filePath = null;
            String _token = message.forwardedFrom != null ? message.forwardedFrom.getAttachment().getToken() : message.attachment.token;
            RealmAttachment _Attachment = getRealmChat().where(RealmAttachment.class).equalTo(RealmAttachmentFields.TOKEN, _token).findFirst();

            if (_Attachment != null) {
                _filePath = _Attachment.getLocalFilePath();
            } else if (message.attachment != null) {
                _filePath = message.attachment.getLocalFilePath();
            }

            if (_filePath == null || _filePath.length() == 0) {
                return;
            }

            Intent intent = HelperMimeType.appropriateProgram(_filePath);
            if (intent != null) {
                try {
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivity(intent);
                } catch (Exception e) {
                    // to prevent from 'No Activity found to handle Intent'
                    e.printStackTrace();
                }
            }
        }
        //realm.close();
    }

    @Override
    public void onDownloadAllEqualCashId(String cashId, String messageID) {

        int start = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();

        for (int i = start; i < mAdapter.getItemCount() && i < start + 15; i++) {
            try {
                AbstractMessage item = mAdapter.getAdapterItem(i);
                if (item.mMessage.hasAttachment()) {
                    if (item.mMessage.getAttachment().cashID != null && item.mMessage.getAttachment().cashID.equals(cashId) && (!item.mMessage.messageID.equals(messageID))) {
                        mAdapter.notifyItemChanged(i);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onItemShowingMessageId(final StructMessageInfo messageInfo) {
        /**
         * if in current room client have new message that not seen yet
         * after first new message come in the view change view for unread count
         */
        if (firstUnreadMessageInChat != null && firstUnreadMessageInChat.isManaged() && firstUnreadMessageInChat.isValid() && !firstUnreadMessageInChat.isDeleted() && firstUnreadMessageInChat.getMessageId() == parseLong(messageInfo.messageID)) {
            countNewMessage = 0;
            txtNewUnreadMessage.setVisibility(View.GONE);
            txtNewUnreadMessage.setText(countNewMessage + "");

            firstUnreadMessageInChat = null;
        }

        if (chatType != CHANNEL && (!messageInfo.isSenderMe() && messageInfo.status != null && !messageInfo.status.equals(ProtoGlobal.RoomMessageStatus.SEEN.toString()) & !messageInfo.status.equals(ProtoGlobal.RoomMessageStatus.LISTENED.toString()))) {

            /**
             * set message status SEEN for avoid from run this block in each bindView
             */
            messageInfo.status = ProtoGlobal.RoomMessageStatus.SEEN.toString();

            //+final Realm realm = Realm.getDefaultInstance();
            getRealmChat().executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    /**
                     * check SEEN and LISTENED for avoid from duplicate send status request in enter to chat
                     * because in enter ro chat fetchMessage method will be send status so client shouldn't
                     * send status again
                     */
                    RealmRoomMessage realmRoomMessage = realm.where(RealmRoomMessage.class).equalTo(RealmRoomMessageFields.MESSAGE_ID, parseLong(messageInfo.messageID)).notEqualTo(RealmRoomMessageFields.STATUS, ProtoGlobal.RoomMessageStatus.SEEN.toString()).notEqualTo(RealmRoomMessageFields.STATUS, ProtoGlobal.RoomMessageStatus.LISTENED.toString()).findFirst();
                    if (realmRoomMessage != null) {
                        final RealmClientCondition realmClientCondition = realm.where(RealmClientCondition.class).equalTo(RealmClientConditionFields.ROOM_ID, mRoomId).findFirst();
                        if (realmClientCondition != null) {
                            if (!realmRoomMessage.getStatus().equalsIgnoreCase(ProtoGlobal.RoomMessageStatus.SEEN.toString())) {
                                realmRoomMessage.setStatus(ProtoGlobal.RoomMessageStatus.SEEN.toString());

                                RealmOfflineSeen realmOfflineSeen = realm.createObject(RealmOfflineSeen.class, SUID.id().get());
                                realmOfflineSeen.setOfflineSeen(realmRoomMessage.getMessageId());
                                realm.copyToRealmOrUpdate(realmOfflineSeen);
                                if (realmClientCondition.getOfflineSeen() != null) {
                                    realmClientCondition.getOfflineSeen().add(realmOfflineSeen);
                                } else {
                                    RealmList<RealmOfflineSeen> offlineSeenList = new RealmList<>();
                                    offlineSeenList.add(realmOfflineSeen);
                                    realmClientCondition.setOfflineSeen(offlineSeenList);
                                }
                            }
                        }

                        G.chatUpdateStatusUtil.sendUpdateStatus(chatType, mRoomId, realmRoomMessage.getMessageId(), ProtoGlobal.RoomMessageStatus.SEEN);
                    }
                }
                //}, new Realm.Transaction.OnSuccess() {
                //    @Override
                //    public void onSuccess() {
                //        realm.close();
                //    }
                //}, new Realm.Transaction.OnError() {
                //    @Override
                //    public void onError(Throwable error) {
                //        realm.close();
                //    }
            });
        }
    }

    @Override
    public void onPlayMusic(String messageId) {

        if (messageId != null && messageId.length() > 0) {

            try {
                if (MusicPlayer.downloadNextMusic(messageId)) {
                    mAdapter.notifyDataSetChanged();
                }
            } catch (Exception e) {
                HelperLog.setErrorLog("Activity chat  onPlayMusic    " + e.toString());
            }
        }
    }

    @Override
    public void onContainerClick(View view, final StructMessageInfo message, int pos) {

        final MaterialDialog dialog = new MaterialDialog.Builder(G.fragmentActivity).customView(R.layout.chat_popup_dialog_custom, true).build();

        View v = dialog.getCustomView();
        if (v == null) {
            return;
        }

        DialogAnimation.animationDown(dialog);
        dialog.show();

        ViewGroup rootReplay = (ViewGroup) v.findViewById(R.id.dialog_root_item1_notification);
        ViewGroup rootCopy = (ViewGroup) v.findViewById(R.id.dialog_root_item2_notification);
        ViewGroup rootShare = (ViewGroup) v.findViewById(R.id.dialog_root_item3_notification);
        ViewGroup rootForward = (ViewGroup) v.findViewById(R.id.dialog_root_item4_notification);
        ViewGroup rootDelete = (ViewGroup) v.findViewById(R.id.dialog_root_item5_notification);
        ViewGroup rootEdit = (ViewGroup) v.findViewById(R.id.dialog_root_item6_notification);
        final ViewGroup rootSaveToDownload = (ViewGroup) v.findViewById(R.id.dialog_root_item7_notification);

        TextView txtItemReplay = (TextView) v.findViewById(R.id.dialog_text_item1_notification);
        TextView txtItemCopy = (TextView) v.findViewById(R.id.dialog_text_item2_notification);
        TextView txtItemShare = (TextView) v.findViewById(R.id.dialog_text_item3_notification);
        TextView txtItemForward = (TextView) v.findViewById(R.id.dialog_text_item4_notification);
        TextView txtItemDelete = (TextView) v.findViewById(R.id.dialog_text_item5_notification);
        TextView txtItemEdit = (TextView) v.findViewById(R.id.dialog_text_item6_notification);
        final TextView txtItemSaveToDownload = (TextView) v.findViewById(R.id.dialog_text_item7_notification);

        TextView iconReplay = (TextView) v.findViewById(R.id.dialog_icon_item1_notification);
        iconReplay.setText(G.context.getResources().getString(R.string.md_back_arrow_reply));

        TextView iconCopy = (TextView) v.findViewById(R.id.dialog_icon_item2_notification);
        iconCopy.setText(G.context.getResources().getString(R.string.md_copy));

        TextView iconShare = (TextView) v.findViewById(R.id.dialog_icon_item3_notification);
        iconShare.setText(G.context.getResources().getString(R.string.md_share_button));

        TextView iconForward = (TextView) v.findViewById(R.id.dialog_icon_item4_notification);
        iconForward.setText(G.context.getResources().getString(R.string.md_forward));

        TextView iconDelete = (TextView) v.findViewById(R.id.dialog_icon_item5_notification);
        iconDelete.setText(G.context.getResources().getString(R.string.md_rubbish_delete_file));

        TextView iconEdit = (TextView) v.findViewById(R.id.dialog_icon_item6_notification);
        iconEdit.setText(G.context.getResources().getString(R.string.md_edit));

        TextView iconItemSaveToDownload = (TextView) v.findViewById(R.id.dialog_icon_item7_notification);
        iconItemSaveToDownload.setText(G.context.getResources().getString(R.string.md_save));

        @ArrayRes int itemsRes = 0;
        switch (message.messageType) {
            case TEXT:
                //itemsRes = R.array.textMessageDialogItems;

                txtItemReplay.setText(R.string.replay_item_dialog);
                txtItemCopy.setText(R.string.copy_item_dialog);
                txtItemShare.setText(R.string.share_item_dialog);
                txtItemForward.setText(R.string.forward_item_dialog);
                txtItemDelete.setText(R.string.delete_item_dialog);
                txtItemEdit.setText(R.string.edit_item_dialog);

                rootReplay.setVisibility(View.VISIBLE);
                rootCopy.setVisibility(View.VISIBLE);
                rootShare.setVisibility(View.VISIBLE);
                rootForward.setVisibility(View.VISIBLE);
                rootDelete.setVisibility(View.VISIBLE);
                rootEdit.setVisibility(View.VISIBLE);

                break;
            case FILE_TEXT:
            case IMAGE_TEXT:
            case VIDEO_TEXT:
            case AUDIO_TEXT:
            case GIF_TEXT:
                //itemsRes = R.array.fileTextMessageDialogItems;

                txtItemReplay.setText(R.string.replay_item_dialog);
                txtItemCopy.setText(R.string.copy_item_dialog);
                txtItemShare.setText(R.string.share_item_dialog);
                txtItemForward.setText(R.string.forward_item_dialog);
                txtItemDelete.setText(R.string.delete_item_dialog);
                txtItemEdit.setText(R.string.edit_item_dialog);

                rootReplay.setVisibility(View.VISIBLE);
                rootCopy.setVisibility(View.VISIBLE);
                rootShare.setVisibility(View.VISIBLE);
                rootForward.setVisibility(View.VISIBLE);
                rootDelete.setVisibility(View.VISIBLE);
                rootEdit.setVisibility(View.VISIBLE);
                rootSaveToDownload.setVisibility(View.VISIBLE);
                break;
            case FILE:
            case IMAGE:
            case VIDEO:
            case AUDIO:
            case VOICE:
            case GIF:

                txtItemReplay.setText(R.string.replay_item_dialog);
                txtItemShare.setText(R.string.share_item_dialog);
                txtItemForward.setText(R.string.forward_item_dialog);
                txtItemDelete.setText(R.string.delete_item_dialog);

                rootReplay.setVisibility(View.VISIBLE);
                rootShare.setVisibility(View.VISIBLE);
                rootForward.setVisibility(View.VISIBLE);
                rootDelete.setVisibility(View.VISIBLE);
                rootSaveToDownload.setVisibility(View.VISIBLE);

                //itemsRes = R.array.fileMessageDialogItems;
                break;
            case LOCATION:
            case CONTACT:
            case LOG:

                txtItemReplay.setText(R.string.replay_item_dialog);
                txtItemShare.setText(R.string.share_item_dialog);
                txtItemForward.setText(R.string.forward_item_dialog);
                txtItemDelete.setText(R.string.delete_item_dialog);
                //itemsRes = R.array.otherMessageDialogItems;

                rootReplay.setVisibility(View.VISIBLE);
                rootShare.setVisibility(View.VISIBLE);
                rootForward.setVisibility(View.VISIBLE);
                rootDelete.setVisibility(View.VISIBLE);

                break;
        }

        //if (itemsRes != 0) {
        // Arrays.asList returns fixed size, doing like this fixes remove object
        // UnsupportedOperationException exception
        //List<String> items = new LinkedList<>(Arrays.asList(getResources().getStringArray(itemsRes)));

        //+Realm realm = Realm.getDefaultInstance();
        RealmRoom realmRoom = getRealmChat().where(RealmRoom.class).equalTo(RealmRoomFields.ID, message.roomId).findFirst();
        if (realmRoom != null) {
            // if user clicked on any message which he wasn't its sender, remove edit mList option
            if (chatType == CHANNEL) {
                if (channelRole == ChannelChatRole.MEMBER) {
                    rootEdit.setVisibility(View.GONE);
                    rootReplay.setVisibility(View.GONE);
                    rootDelete.setVisibility(View.GONE);

                    //items.remove(G.context.getResources().getString(R.string.edit_item_dialog));
                    //items.remove(G.context.getResources().getString(R.string.replay_item_dialog));
                    //items.remove(G.context.getResources().getString(R.string.delete_item_dialog));
                }
                final long senderId = G.userId;
                ChannelChatRole roleSenderMessage = null;
                RealmChannelRoom realmChannelRoom = realmRoom.getChannelRoom();
                RealmList<RealmMember> realmMembers = realmChannelRoom.getMembers();
                for (RealmMember rm : realmMembers) {
                    if (rm.getPeerId() == parseLong(message.senderID)) {
                        roleSenderMessage = ChannelChatRole.valueOf(rm.getRole());
                    }
                }
                if (!G.authorHash.equals(message.authorHash)) {  // if message dose'nt belong to owner
                    if (channelRole == ChannelChatRole.MEMBER) {

                        //items.remove(G.context.getResources().getString(R.string.delete_item_dialog));
                        rootDelete.setVisibility(View.GONE);
                    } else if (channelRole == ChannelChatRole.MODERATOR) {
                        if (roleSenderMessage == ChannelChatRole.MODERATOR || roleSenderMessage == ChannelChatRole.ADMIN || roleSenderMessage == ChannelChatRole.OWNER) {
                            //items.remove(G.context.getResources().getString(R.string.delete_item_dialog));
                            rootDelete.setVisibility(View.GONE);
                        }
                    } else if (channelRole == ChannelChatRole.ADMIN) {
                        if (roleSenderMessage == ChannelChatRole.OWNER || roleSenderMessage == ChannelChatRole.ADMIN) {
                            //items.remove(G.context.getResources().getString(R.string.delete_item_dialog));
                            rootDelete.setVisibility(View.GONE);
                        }
                    }
                    //items.remove(G.context.getResources().getString(R.string.edit_item_dialog));
                    rootEdit.setVisibility(View.GONE);
                }
            } else if (chatType == GROUP) {

                final long senderId = G.userId;

                GroupChatRole roleSenderMessage = null;
                RealmGroupRoom realmGroupRoom = realmRoom.getGroupRoom();
                RealmList<RealmMember> realmMembers = realmGroupRoom.getMembers();
                for (RealmMember rm : realmMembers) {
                    if (rm.getPeerId() == parseLong(message.senderID)) {
                        roleSenderMessage = GroupChatRole.valueOf(rm.getRole());
                    }
                }
                if (!G.authorHash.equals(message.authorHash)) {  // if message dose'nt belong to owner
                    if (groupRole == GroupChatRole.MEMBER) {
                        //items.remove(G.context.getResources().getString(R.string.delete_item_dialog));
                        rootDelete.setVisibility(View.GONE);
                    } else if (groupRole == GroupChatRole.MODERATOR) {
                        if (roleSenderMessage == GroupChatRole.MODERATOR || roleSenderMessage == GroupChatRole.ADMIN || roleSenderMessage == GroupChatRole.OWNER) {
                            //items.remove(G.context.getResources().getString(R.string.delete_item_dialog));
                            rootDelete.setVisibility(View.GONE);
                        }
                    } else if (groupRole == GroupChatRole.ADMIN) {
                        if (roleSenderMessage == GroupChatRole.OWNER || roleSenderMessage == GroupChatRole.ADMIN) {
                            //items.remove(G.context.getResources().getString(R.string.delete_item_dialog));
                            rootDelete.setVisibility(View.GONE);
                        }
                    }
                    //items.remove(G.context.getResources().getString(R.string.edit_item_dialog));
                    rootEdit.setVisibility(View.GONE);
                }
            } else if (realmRoom.getReadOnly()) {
                rootReplay.setVisibility(View.GONE);
            } else {
                if (!message.senderID.equalsIgnoreCase(Long.toString(G.userId))) {
                    //items.remove(G.context.getResources().getString(R.string.edit_item_dialog));
                    rootEdit.setVisibility(View.GONE);
                }
            }

            //realm.close();
        }
        //}

        String _savedFolderName = "";
        if (message.messageType.toString().contains("IMAGE") || message.messageType.toString().contains("VIDEO") || message.messageType.toString().contains("GIF")) {
            _savedFolderName = G.context.getResources().getString(R.string.save_to_gallery);
        } else if (message.messageType.toString().contains("AUDIO") || message.messageType.toString().contains("VOICE")) {
            _savedFolderName = G.context.getResources().getString(R.string.save_to_Music);
        } else {
            _savedFolderName = G.context.getResources().getString(R.string.saveToDownload_item_dialog);
        }

        txtItemSaveToDownload.setText(_savedFolderName);
        rootReplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                replay(message);
            }
        });
        rootCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                ClipboardManager clipboard = (ClipboardManager) G.fragmentActivity.getSystemService(CLIPBOARD_SERVICE);
                String _text = message.forwardedFrom != null ? message.forwardedFrom.getMessage() : message.messageText;
                if (_text != null && _text.length() > 0) {
                    ClipData clip = ClipData.newPlainText("Copied Text", _text);
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(context, R.string.text_copied, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, R.string.text_is_empty, Toast.LENGTH_SHORT).show();
                }
            }
        });
        rootShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                shearedDataToOtherProgram(message);
            }
        });
        rootForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                // forward selected messages to room list for selecting room
                if (mAdapter != null) {
                    //finish();
                    finishChat();
                    mForwardMessages = new ArrayList<>(Arrays.asList(Parcels.wrap(message)));
                }
            }
        });
        rootDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                G.handler.post(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                        // remove deleted message from adapter
                        mAdapter.removeMessage(parseLong(message.messageID));

                        // remove tag from edtChat if the
                        // message has deleted
                        if (edtChat.getTag() != null && edtChat.getTag() instanceof StructMessageInfo) {
                            if (Long.toString(parseLong(message.messageID)).equals(((StructMessageInfo) edtChat.getTag()).messageID)) {
                                edtChat.setTag(null);
                            }
                        }
                    }
                });
                //final Realm realmCondition = Realm.getDefaultInstance();
                final RealmClientCondition realmClientCondition = getRealmChat().where(RealmClientCondition.class).equalTo(RealmClientConditionFields.ROOM_ID, message.roomId).findFirst();

                if (realmClientCondition != null) {

                    getRealmChat().executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {

                            if (realm.where(RealmOfflineDelete.class).equalTo(RealmOfflineDeleteFields.OFFLINE_DELETE, parseLong(message.messageID)).findFirst() == null) {
                                RealmRoomMessage roomMessage = realm.where(RealmRoomMessage.class).equalTo(RealmRoomMessageFields.MESSAGE_ID, parseLong(message.messageID)).findFirst();
                                if (roomMessage != null) {
                                    roomMessage.setDeleted(true);
                                }
                                RealmOfflineDelete realmOfflineDelete = realm.createObject(RealmOfflineDelete.class, SUID.id().get());
                                realmOfflineDelete.setOfflineDelete(parseLong(message.messageID));
                                realmClientCondition.getOfflineDeleted().add(realmOfflineDelete);
                                // delete message
                                if (chatType == GROUP) {
                                    new RequestGroupDeleteMessage().groupDeleteMessage(mRoomId, parseLong(message.messageID));
                                } else if (chatType == CHAT) {
                                    new RequestChatDeleteMessage().chatDeleteMessage(mRoomId, parseLong(message.messageID));
                                } else if (chatType == CHANNEL) {
                                    new RequestChannelDeleteMessage().channelDeleteMessage(mRoomId, parseLong(message.messageID));
                                }
                            }
                        }
                    });
                }
                //realmCondition.close();
            }
        });
        rootEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                // edit message
                // put message text to EditText
                if (message.messageText != null && !message.messageText.isEmpty()) {
                    edtChat.setText(message.messageText);
                    edtChat.setSelection(0, edtChat.getText().length());
                    // put message object to edtChat's tag to obtain it later and
                    // found is user trying to edit a message
                    edtChat.setTag(message);
                }
            }
        });
        rootSaveToDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                final String _dPath = message.getAttachment().localFilePath != null ? message.getAttachment().localFilePath : AndroidUtils.getFilePathWithCashId(message.getAttachment().cashID, message.getAttachment().name, message.messageType);

                if (new File(_dPath).exists()) {

                    if (txtItemSaveToDownload.getText().toString().equalsIgnoreCase(G.context.getResources().getString(R.string.saveToDownload_item_dialog))) {

                        HelperSaveFile.saveFileToDownLoadFolder(_dPath, message.getAttachment().name, HelperSaveFile.FolderType.download, R.string.file_save_to_download_folder);
                    } else if (txtItemSaveToDownload.getText().toString().equalsIgnoreCase(G.context.getResources().getString(R.string.save_to_Music))) {
                        HelperSaveFile.saveFileToDownLoadFolder(_dPath, message.getAttachment().name, HelperSaveFile.FolderType.music, R.string.save_to_music_folder);
                    } else if (txtItemSaveToDownload.getText().toString().equalsIgnoreCase(G.context.getResources().getString(R.string.save_to_gallery))) {

                        if (message.messageType.toString().contains("VIDEO")) {
                            HelperSaveFile.saveFileToDownLoadFolder(_dPath, message.getAttachment().name, HelperSaveFile.FolderType.video, R.string.file_save_to_video_folder);
                        } else if (message.messageType.toString().contains("GIF")) {
                            HelperSaveFile.saveFileToDownLoadFolder(_dPath, message.getAttachment().name, HelperSaveFile.FolderType.gif, R.string.file_save_to_picture_folder);
                        } else {
                            HelperSaveFile.saveFileToDownLoadFolder(_dPath, message.getAttachment().name, HelperSaveFile.FolderType.image, R.string.picture_save_to_galary);
                        }
                    }
                } else {

                    ProtoGlobal.RoomMessageType _messageType = message.forwardedFrom != null ? message.forwardedFrom.getMessageType() : message.messageType;
                    String _cashid = message.forwardedFrom != null ? message.forwardedFrom.getAttachment().getCacheId() : message.getAttachment().cashID;
                    String _name = message.forwardedFrom != null ? message.forwardedFrom.getAttachment().getName() : message.getAttachment().name;
                    String _token = message.forwardedFrom != null ? message.forwardedFrom.getAttachment().getToken() : message.getAttachment().token;
                    Long _size = message.forwardedFrom != null ? message.forwardedFrom.getAttachment().getSize() : message.getAttachment().size;

                    if (_cashid == null) {
                        return;
                    }
                    ProtoFileDownload.FileDownload.Selector selector = ProtoFileDownload.FileDownload.Selector.FILE;

                    final String _path = AndroidUtils.getFilePathWithCashId(_cashid, _name, _messageType);
                    if (_token != null && _token.length() > 0 && _size > 0) {
                        HelperDownloadFile.startDownload(message.messageID, _token, _cashid, _name, _size, selector, _path, 0, new HelperDownloadFile.UpdateListener() {
                            @Override
                            public void OnProgress(String path, int progress) {

                                if (progress == 100) {

                                    if (canUpdateAfterDownload) {
                                        G.handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                if (txtItemSaveToDownload.getText().toString().equalsIgnoreCase(G.context.getResources().getString(R.string.saveToDownload_item_dialog))) {

                                                    HelperSaveFile.saveFileToDownLoadFolder(_dPath, message.getAttachment().name, HelperSaveFile.FolderType.download, R.string.file_save_to_download_folder);
                                                } else if (txtItemSaveToDownload.getText().toString().equalsIgnoreCase(G.context.getResources().getString(R.string.save_to_Music))) {
                                                    HelperSaveFile.saveFileToDownLoadFolder(_dPath, message.getAttachment().name, HelperSaveFile.FolderType.music, R.string.save_to_music_folder);
                                                } else if (txtItemSaveToDownload.getText().toString().equalsIgnoreCase(G.context.getResources().getString(R.string.save_to_gallery))) {

                                                    if (message.messageType.toString().contains("VIDEO")) {
                                                        HelperSaveFile.saveFileToDownLoadFolder(_dPath, message.getAttachment().name, HelperSaveFile.FolderType.video, R.string.file_save_to_video_folder);
                                                    } else if (message.messageType.toString().contains("GIF")) {
                                                        HelperSaveFile.saveFileToDownLoadFolder(_dPath, message.getAttachment().name, HelperSaveFile.FolderType.gif, R.string.file_save_to_picture_folder);
                                                    } else {
                                                        HelperSaveFile.saveFileToDownLoadFolder(_dPath, message.getAttachment().name, HelperSaveFile.FolderType.image, R.string.picture_save_to_galary);
                                                    }
                                                }
                                            }
                                        });
                                    }
                                }
                            }

                            @Override
                            public void OnError(String token) {

                            }
                        });
                    }

                    onDownloadAllEqualCashId(_cashid, message.messageID);
                }
            }
        });
    }

    @Override
    public void onFailedMessageClick(View view, final StructMessageInfo message, final int pos) {
        final List<StructMessageInfo> failedMessages = mAdapter.getFailedMessages();
        new ResendMessage(G.fragmentActivity, new IResendMessage() {
            @Override
            public void deleteMessage() {
                if (pos >= 0 && mAdapter.getAdapterItemCount() > pos) {
                    mAdapter.remove(pos);
                }
            }

            @Override
            public void resendMessage() {
                mAdapter.updateMessageStatus(parseLong(message.messageID), ProtoGlobal.RoomMessageStatus.SENDING);
            }

            @Override
            public void resendAllMessages() {
                for (int i = 0; i < failedMessages.size(); i++) {
                    final int j = i;
                    G.handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter.updateMessageStatus(parseLong(failedMessages.get(j).messageID), ProtoGlobal.RoomMessageStatus.SENDING);
                        }
                    }, 1000 * i);
                }
            }
        }, parseLong(message.messageID), failedMessages);
    }

    @Override
    public void onReplyClick(RealmRoomMessage replyMessage) {

        long replyMessageId = replyMessage.getMessageId();
        /**
         * when i add message to RealmRoomMessage(putOrUpdate) set (replyMessageId * (-1))
         * so i need to (replyMessageId * (-1)) again for use this messageId
         */
        int position = mAdapter.findPositionByMessageId((replyMessageId * (-1)));
        if (position == -1) {
            position = mAdapter.findPositionByMessageId(replyMessageId);
        }

        recyclerView.scrollToPosition(position);
    }

    @Override
    public void onSetAction(final long roomId, final long userIdR, final ProtoGlobal.ClientAction clientAction) {
        //Realm realm = Realm.getDefaultInstance();
        getRealmChat().executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                if (mRoomId == roomId && (userId != userIdR || (isCloudRoom))) {
                    final String action = HelperGetAction.getAction(roomId, chatType, clientAction);

                    final RealmRoom realmRoom = realm.where(RealmRoom.class).equalTo(RealmRoomFields.ID, roomId).findFirst();
                    if (realmRoom != null) {
                        realmRoom.setActionState(action, userId);
                    }

                    G.handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (action != null) {
                                // avi.setVisibility(View.VISIBLE);
                                //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                                //    viewGroupLastSeen.setLayoutDirection(View.LAYOUT_DIRECTION_LOCALE);
                                //    //txtLastSeen.setTextDirection(View.TEXT_DIRECTION_LTR);
                                //}
                                ViewMaker.setLayoutDirection(viewGroupLastSeen, View.LAYOUT_DIRECTION_LOCALE);
                                txtLastSeen.setText(action);
                            } else if (chatType == CHAT) {
                                if (isCloudRoom) {
                                    txtLastSeen.setText(G.context.getResources().getString(R.string.chat_with_yourself));
                                } else {
                                    if (userStatus != null) {
                                        if (userStatus.equals(ProtoGlobal.RegisteredUser.Status.EXACTLY.toString())) {
                                            txtLastSeen.setText(LastSeenTimeUtil.computeTime(chatPeerId, userTime, true, false));
                                        } else {
                                            txtLastSeen.setText(userStatus);
                                        }
                                    }
                                }
                                // avi.setVisibility(View.GONE);
                                //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                                //    viewGroupLastSeen.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                                //    //txtLastSeen.setTextDirection(View.TEXT_DIRECTION_LTR);
                                //}
                                ViewMaker.setLayoutDirection(viewGroupLastSeen, View.LAYOUT_DIRECTION_LTR);
                                //txtLastSeen.setText(userStatus);
                            } else if (chatType == GROUP) {
                                //avi.setVisibility(View.GONE);
                                //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                                //    viewGroupLastSeen.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                                //    //txtLastSeen.setTextDirection(View.TEXT_DIRECTION_LTR);
                                //}
                                ViewMaker.setLayoutDirection(viewGroupLastSeen, View.LAYOUT_DIRECTION_LTR);

                                if (groupParticipantsCountLabel != null && HelperString.isNumeric(groupParticipantsCountLabel) && Integer.parseInt(groupParticipantsCountLabel) == 1) {
                                    txtLastSeen.setText(groupParticipantsCountLabel + " " + G.context.getResources().getString(R.string.one_member_chat));
                                } else {
                                    txtLastSeen.setText(groupParticipantsCountLabel + " " + G.context.getResources().getString(R.string.member_chat));
                                }
                            }

                            // change english number to persian number
                            if (HelperCalander.isLanguagePersian) txtLastSeen.setText(HelperCalander.convertToUnicodeFarsiNumber(txtLastSeen.getText().toString()));
                        }
                    });
                }
            }
        });
        //realm.close();
    }

    @Override
    public void onUserUpdateStatus(long userId, final long time, final String status) {
        if (chatType == CHAT && chatPeerId == userId) {
            userStatus = AppUtils.getStatsForUser(status);
            setUserStatus(userStatus, time);
        }
    }

    @Override
    public void onLastSeenUpdate(final long userIdR, final String showLastSeen) {
        G.handler.post(new Runnable() {
            @Override
            public void run() {
                if (chatType == CHAT && userIdR == chatPeerId && userId != userIdR) { // userId != userIdR means that , this isn't update status for own user
                    txtLastSeen.setText(showLastSeen);
                    //  avi.setVisibility(View.GONE);
                    //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    //    viewGroupLastSeen.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                    //    //txtLastSeen.setTextDirection(View.TEXT_DIRECTION_LTR);
                    //}
                    ViewMaker.setLayoutDirection(viewGroupLastSeen, View.LAYOUT_DIRECTION_LTR);
                    // change english number to persian number
                    if (HelperCalander.isLanguagePersian) txtLastSeen.setText(HelperCalander.convertToUnicodeFarsiNumber(txtLastSeen.getText().toString()));
                }
            }
        });
    }

    /**
     * GroupAvatar and ChannelAvatar
     */
    @Override
    public void onAvatarAdd(final long roomId, ProtoGlobal.Avatar avatar) {

        HelperAvatar.getAvatar(roomId, HelperAvatar.AvatarType.ROOM, true, new OnAvatarGet() {
            @Override
            public void onAvatarGet(final String avatarPath, long ownerId) {
                G.handler.post(new Runnable() {
                    @Override
                    public void run() {

                        if (!isCloudRoom) {
                            G.imageLoader.displayImage(AndroidUtils.suitablePath(avatarPath), imvUserPicture);
                        }
                    }
                });
            }

            @Override
            public void onShowInitials(final String initials, final String color) {
                G.handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (!isCloudRoom) {
                            imvUserPicture.setImageBitmap(net.iGap.helper.HelperImageBackColor.drawAlphabetOnPicture((int) imvUserPicture.getContext().getResources().getDimension(R.dimen.dp60), initials, color));
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onAvatarAddError() {
        //empty
    }

    /**
     * Channel Message Reaction
     */

    @Override
    public void onChannelAddMessageReaction(final long roomId, final long messageId, final String reactionCounterLabel, final ProtoGlobal.RoomMessageReaction reaction, final long forwardedMessageId) {
        G.handler.post(new Runnable() {
            @Override
            public void run() {
                mAdapter.updateVote(roomId, messageId, reactionCounterLabel, reaction, forwardedMessageId);
            }
        });
    }

    @Override
    public void onError(int majorCode, int minorCode) {
        //empty
    }

    @Override
    public void onChannelGetMessagesStats(final List<ProtoChannelGetMessagesStats.ChannelGetMessagesStatsResponse.Stats> statsList) {

        if (mAdapter != null) {
            for (final ProtoChannelGetMessagesStats.ChannelGetMessagesStatsResponse.Stats stats : statsList) {
                G.handler.post(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.updateMessageState(stats.getMessageId(), stats.getThumbsUpLabel(), stats.getThumbsDownLabel(), stats.getViewsLabel());
                    }
                });
            }
        }
    }

    @Override
    public void onChatDelete(long roomId) {
        if (roomId == mRoomId) {
            //  finish();
            finishChat();
        }
    }

    @Override
    public void onChatDeleteError(int majorCode, int minorCode) {

    }


    @Override
    public void onBackgroundChanged(final String backgroundPath) {
        G.handler.post(new Runnable() {
            @Override
            public void run() {
                if (imgBackGround != null) {
                    File f = new File(backgroundPath);
                    if (f.exists()) {
                        Drawable d = Drawable.createFromPath(f.getAbsolutePath());
                        imgBackGround.setImageDrawable(d);
                    }
                }
            }
        });
    }

    /**
     * *************************** common method ***************************
     */

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    private Realm getRealmChat() {
        if (realmChat == null || realmChat.isClosed()) {
            realmChat = Realm.getDefaultInstance();
        }
        return realmChat;
    }

    private void updateShowItemInScreen() {
        /**
         * after comeback from other activity or background  the view should update
         */
        try {
            // this only notify item that show on the screen and no more
            recyclerView.getAdapter().notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * detect that editText have character or just have space
     */
    private boolean isMessageWrote() {
        return !getWrittenMessage().isEmpty();
    }

    /**
     * get message and remove space from start and end
     */
    private String getWrittenMessage() {
        return edtChat.getText().toString().trim();
    }

    /**
     * clear history for this room
     */
    public void clearHistory(long chatId) {
        llScrollNavigate.setVisibility(View.GONE);
        saveMessageIdPositionState(0);
        RealmRoomMessage.clearHistoryMessage(chatId);
        addToView = true;
    }

    /**
     * message will be replied or no
     */
    private boolean userTriesReplay() {
        return mReplayLayout != null && mReplayLayout.getTag() instanceof StructMessageInfo;
    }

    /**
     * if userTriesReplay() is true use from this method
     */
    private long getReplyMessageId() {
        return parseLong(((StructMessageInfo) mReplayLayout.getTag()).messageID);
    }

    /**
     * if userTriesReplay() is true use from this method
     */
    private void clearReplyView() {
        if (mReplayLayout != null) {
            mReplayLayout.setTag(null);
            G.handler.post(new Runnable() {
                @Override
                public void run() {
                    mReplayLayout.setVisibility(View.GONE);
                }
            });
        }
    }

    private void hideProgress() {
        G.handler.post(new Runnable() {
            @Override
            public void run() {
                if (prgWaiting != null) {
                    prgWaiting.setVisibility(View.GONE);
                }
            }
        });
    }

    /**
     * clear all items that exist in view
     */
    private void clearAdapterItems() {
        mAdapter.clear();
        recyclerView.removeAllViews();
    }

    /**
     * client should send request for get user info because need to update user online timing
     */
    private void getUserInfo() {
        if (chatType == CHAT) {
            new RequestUserInfo().userInfo(chatPeerId);
        }
    }

    /**
     * call this method for set avatar for this room and this method
     * will be automatically detect id and chat type for show avatar
     */
    private void setAvatar() {
        long idForGetAvatar;
        HelperAvatar.AvatarType type;
        if (chatType == CHAT) {
            idForGetAvatar = chatPeerId;
            type = HelperAvatar.AvatarType.USER;
        } else {
            idForGetAvatar = mRoomId;
            type = HelperAvatar.AvatarType.ROOM;
        }

        HelperAvatar.getAvatar(idForGetAvatar, type, true, new OnAvatarGet() {
            @Override
            public void onAvatarGet(final String avatarPath, long ownerId) {
                G.handler.post(new Runnable() {
                    @Override
                    public void run() {

                        G.imageLoader.displayImage(AndroidUtils.suitablePath(avatarPath), imvUserPicture);
                    }
                });
            }

            @Override
            public void onShowInitials(final String initials, final String color) {
                G.handler.post(new Runnable() {
                    @Override
                    public void run() {

                        imvUserPicture.setImageBitmap(net.iGap.helper.HelperImageBackColor.drawAlphabetOnPicture((int) imvUserPicture.getContext().getResources().getDimension(R.dimen.dp60), initials, color));
                    }
                });
            }
        });
    }

    private void resetAndGetFromEnd() {
        llScrollNavigate.setVisibility(View.GONE);
        firstUnreadMessageInChat = null;
        resetMessagingValue();
        countNewMessage = 0;
        txtNewUnreadMessage.setVisibility(View.GONE);
        txtNewUnreadMessage.setText(countNewMessage + "");
        getMessages();
    }

    private ArrayList<Parcelable> getMessageStructFromSelectedItems() {
        ArrayList<Parcelable> messageInfos = new ArrayList<>(mAdapter.getSelectedItems().size());
        for (AbstractMessage item : mAdapter.getSelectedItems()) {
            messageInfos.add(Parcels.wrap(item.mMessage));
        }
        return messageInfos;
    }

    /**
     * show current state for user if this room is chat
     *
     * @param status current state
     * @param time if state is not online set latest online time
     */
    private void setUserStatus(final String status, final long time) {
        G.handler.post(new Runnable() {
            @Override
            public void run() {
                userStatus = status;
                userTime = time;
                if (isCloudRoom) {
                    txtLastSeen.setText(G.context.getResources().getString(R.string.chat_with_yourself));
                    //  avi.setVisibility(View.GONE);
                    //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    //    viewGroupLastSeen.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                    //    //txtLastSeen.setTextDirection(View.TEXT_DIRECTION_LTR);
                    //}
                    ViewMaker.setLayoutDirection(viewGroupLastSeen, View.LAYOUT_DIRECTION_LTR);
                } else {
                    if (status != null) {
                        if (status.equals(ProtoGlobal.RegisteredUser.Status.EXACTLY.toString())) {
                            txtLastSeen.setText(LastSeenTimeUtil.computeTime(chatPeerId, time, true, false));
                        } else {
                            txtLastSeen.setText(status);
                        }
                        // avi.setVisibility(View.GONE);
                        //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                        //    viewGroupLastSeen.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                        //    //txtLastSeen.setTextDirection(View.TEXT_DIRECTION_LTR);
                        //}
                        ViewMaker.setLayoutDirection(viewGroupLastSeen, View.LAYOUT_DIRECTION_LTR);
                        // change english number to persian number
                        if (HelperCalander.isLanguagePersian) txtLastSeen.setText(HelperCalander.convertToUnicodeFarsiNumber(txtLastSeen.getText().toString()));

                        checkAction();
                    }
                }
            }
        });
    }

    private void replay(StructMessageInfo item) {
        if (mAdapter != null) {
            Set<AbstractMessage> messages = mAdapter.getSelectedItems();
            // replay works if only one message selected
            inflateReplayLayoutIntoStub(item == null ? messages.iterator().next().mMessage : item);

            ll_AppBarSelected.setVisibility(View.GONE);

            toolbar.setVisibility(View.VISIBLE);

            mAdapter.deselect();
        }
    }

    private void checkAction() {
        //+Realm realm = Realm.getDefaultInstance();
        final RealmRoom realmRoom = getRealmChat().where(RealmRoom.class).equalTo(RealmRoomFields.ID, mRoomId).findFirst();
        if (realmRoom != null && realmRoom.getActionState() != null) {
            G.handler.post(new Runnable() {
                @Override
                public void run() {
                    if (realmRoom.getActionState() != null && (chatType == GROUP || chatType == CHANNEL) || ((isCloudRoom || (!isCloudRoom && realmRoom.getActionStateUserId() != userId)))) {
                        txtLastSeen.setText(realmRoom.getActionState());
                        //  avi.setVisibility(View.VISIBLE);
                        //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                        //    viewGroupLastSeen.setLayoutDirection(View.LAYOUT_DIRECTION_LOCALE);
                        //    //txtLastSeen.setTextDirection(View.TEXT_DIRECTION_LTR);
                        //}
                        ViewMaker.setLayoutDirection(viewGroupLastSeen, View.LAYOUT_DIRECTION_LTR);
                    } else if (chatType == CHAT) {
                        if (isCloudRoom) {
                            txtLastSeen.setText(G.context.getResources().getString(R.string.chat_with_yourself));
                        } else {
                            if (userStatus != null) {
                                if (userStatus.equals(ProtoGlobal.RegisteredUser.Status.EXACTLY.toString())) {
                                    txtLastSeen.setText(LastSeenTimeUtil.computeTime(chatPeerId, userTime, true, false));
                                } else {
                                    txtLastSeen.setText(userStatus);
                                }
                            }
                        }
                        //  avi.setVisibility(View.GONE);
                        //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                        //    viewGroupLastSeen.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                        //    //txtLastSeen.setTextDirection(View.TEXT_DIRECTION_LTR);
                        //}
                        ViewMaker.setLayoutDirection(viewGroupLastSeen, View.LAYOUT_DIRECTION_LTR);
                    } else if (chatType == GROUP) {
                        //  avi.setVisibility(View.GONE);
                        //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                        //    viewGroupLastSeen.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                        //}
                        ViewMaker.setLayoutDirection(viewGroupLastSeen, View.LAYOUT_DIRECTION_LTR);
                        if (groupParticipantsCountLabel != null && HelperString.isNumeric(groupParticipantsCountLabel) && Integer.parseInt(groupParticipantsCountLabel) == 1) {
                            txtLastSeen.setText(groupParticipantsCountLabel + " " + G.context.getResources().getString(R.string.one_member_chat));
                        } else {
                            txtLastSeen.setText(groupParticipantsCountLabel + " " + G.context.getResources().getString(R.string.member_chat));
                        }
                    }
                    // change english number to persian number
                    if (HelperCalander.isLanguagePersian) txtLastSeen.setText(HelperCalander.convertToUnicodeFarsiNumber(txtLastSeen.getText().toString()));
                }
            });
        }
        //realm.close();
    }

    /**
     * change message status from sending to failed
     *
     * @param fakeMessageId messageId that create when created this message
     */
    private void makeFailed(final long fakeMessageId) {
        // message failed
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                final Realm realm = Realm.getDefaultInstance();
                realm.executeTransactionAsync(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        final RealmRoomMessage message = realm.where(RealmRoomMessage.class).equalTo(RealmRoomMessageFields.MESSAGE_ID, fakeMessageId).findFirst();
                        if (message != null && message.getStatus().equals(ProtoGlobal.RoomMessageStatus.SENDING.toString())) {
                            message.setStatus(ProtoGlobal.RoomMessageStatus.FAILED.toString());
                        }
                    }
                }, new Realm.Transaction.OnSuccess() {
                    @Override
                    public void onSuccess() {
                        final RealmRoomMessage message = realm.where(RealmRoomMessage.class).equalTo(RealmRoomMessageFields.MESSAGE_ID, fakeMessageId).findFirst();
                        if (message != null && message.getStatus().equals(ProtoGlobal.RoomMessageStatus.FAILED.toString())) {
                            chatSendMessageUtil.onMessageFailed(message.getRoomId(), message);
                        }
                        realm.close();
                    }
                });
            }
        });
    }

    private void showErrorDialog(final int time) {

        boolean wrapInScrollView = true;
        final MaterialDialog dialogWait = new MaterialDialog.Builder(G.currentActivity).title(G.context.getResources().getString(R.string.title_limit_chat_to_unknown_contact)).customView(R.layout.dialog_remind_time, wrapInScrollView).positiveText(R.string.B_ok).autoDismiss(false).canceledOnTouchOutside(true).onPositive(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                dialog.dismiss();
            }
        }).show();

        View v = dialogWait.getCustomView();
        if (v == null) {
            return;
        }
        //dialogWait.getActionButton(DialogAction.POSITIVE).setEnabled(true);
        final TextView remindTime = (TextView) v.findViewById(R.id.remindTime);
        final TextView txtText = (TextView) v.findViewById(R.id.textRemindTime);
        txtText.setText(G.context.getResources().getString(R.string.text_limit_chat_to_unknown_contact));
        CountDownTimer countWaitTimer = new CountDownTimer(time * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int seconds = (int) ((millisUntilFinished) / 1000);
                long s = seconds % 60;
                long m = (seconds / 60) % 60;
                long h = (seconds / (60 * 60)) % 24;
                remindTime.setText(String.format("%d:%02d:%02d", h, m, s));
            }

            @Override
            public void onFinish() {
                remindTime.setText("00:00");
            }
        };
        countWaitTimer.start();
    }

    /**
     * update item progress
     */
    private void insertItemAndUpdateAfterStartUpload(int progress, final FileUploadStructure struct) {
        if (progress == 0) {
            G.handler.post(new Runnable() {
                @Override
                public void run() {
                    addItemAfterStartUpload(struct);
                }
            });
        } else if (progress == 100) {
            String messageId = struct.messageId + "";
            for (int i = mAdapter.getAdapterItemCount() - 1; i >= 0; i--) {
                AbstractMessage item = mAdapter.getAdapterItem(i);

                if (item.mMessage != null && item.mMessage.messageID.equals(messageId)) {
                    if (item.mMessage.hasAttachment()) {
                        item.mMessage.attachment.token = struct.token;
                    }
                    break;
                }
            }
        }
    }

    /**
     * add new item to view after start upload
     */
    private void addItemAfterStartUpload(final FileUploadStructure struct) {
        try {
            //Realm realm = Realm.getDefaultInstance();
            RealmRoomMessage roomMessage = getRealmChat().where(RealmRoomMessage.class).equalTo(RealmRoomMessageFields.MESSAGE_ID, struct.messageId).findFirst();
            if (roomMessage != null) {
                AbstractMessage message = null;

                if (mAdapter != null) {
                    message = mAdapter.getItemByFileIdentity(struct.messageId);

                    // message doesn't exists
                    if (message == null) {
                        switchAddItem(new ArrayList<>(Collections.singletonList(StructMessageInfo.convert(getRealmChat(), roomMessage))), false);
                        if (!G.userLogin) {
                            G.handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    makeFailed(struct.messageId);
                                }
                            }, 200);
                        }
                    }
                }
            }
            //realm.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * open profile for this room or user profile if room is chat
     */
    private void goToProfile() {
        if (G.fragmentActivity != null) {
            ((ActivityMain) G.fragmentActivity).lockNavigation();
        }

        if (chatType == CHAT) {
            G.handler.post(new Runnable() {
                @Override
                public void run() {
                    new HelperFragment(FragmentContactsProfile.newInstance(mRoomId, chatPeerId, CHAT.toString())).setReplace(false).load();
                }
            });
        } else if (chatType == GROUP) {

            if (!isChatReadOnly) {
                new HelperFragment(FragmentGroupProfile.newInstance(mRoomId, isNotJoin)).setReplace(false).load();

            }
        } else if (chatType == CHANNEL) {
            new HelperFragment(FragmentChannelProfile.newInstance(mRoomId, isNotJoin)).setReplace(false).load();
        }
    }

    /**
     * copy text
     */
    public void copySelectedItemTextToClipboard() {
        for (AbstractMessage _message : mAdapter.getSelectedItems()) {
            String text = _message.mMessage.forwardedFrom != null ? _message.mMessage.forwardedFrom.getMessage() : _message.mMessage.messageText;

            if (text == null || text.length() == 0) {
                continue;
            }

            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", text);
            clipboard.setPrimaryClip(clip);
        }
        mAdapter.deselect();
        toolbar.setVisibility(View.VISIBLE);
        ll_AppBarSelected.setVisibility(View.GONE);

        clearReplyView();
    }

    /**
     * clear tag from edtChat and remove from view and delete from RealmRoomMessage
     */
    private void clearItem(final long messageId, int position) {
        if (edtChat.getTag() != null && edtChat.getTag() instanceof StructMessageInfo) {
            if (Long.toString(messageId).equals(((StructMessageInfo) edtChat.getTag()).messageID)) {
                edtChat.setTag(null);
            }
        }

        mAdapter.removeMessage(position);

        //+Realm realm = Realm.getDefaultInstance();
        getRealmChat().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmRoomMessage roomMessage = realm.where(RealmRoomMessage.class).equalTo(RealmRoomMessageFields.MESSAGE_ID, messageId).findFirst();
                if (roomMessage != null) {
                    // delete message from database
                    roomMessage.deleteFromRealm();
                }
            }
        });
        //realm.close();
    }

    private void onSelectRoomMenu(String message, int item) {
        switch (message) {
            case "txtMuteNotification":
                muteNotification(item);
                break;
            case "txtClearHistory":
                clearHistory(item);
                break;
            case "txtDeleteChat":
                deleteChat(item);
                break;
        }
    }

    private void deleteChat(final int chatId) {
        new RequestChatDelete().chatDelete(chatId);
    }

    private void muteNotification(final int item) {
        //+Realm realm = Realm.getDefaultInstance();

        isMuteNotification = !isMuteNotification;
        getRealmChat().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmRoom realmRoom = realm.where(RealmRoom.class).equalTo(RealmRoomFields.ID, item).findFirst();
                if (realmRoom != null) {
                    realmRoom.setMute(isMuteNotification);
                }
            }
        });

        if (isMuteNotification) {
            ((TextView) rootView.findViewById(R.id.chl_txt_mute_channel)).setText(R.string.unmute);
            iconMute.setVisibility(View.VISIBLE);
        } else {
            ((TextView) rootView.findViewById(R.id.chl_txt_mute_channel)).setText(R.string.mute);
            iconMute.setVisibility(View.GONE);
        }

        //realm.close();
    }

    private void setBtnDownVisible(RealmRoomMessage realmRoomMessage) {
        if (isEnd()) {
            scrollToEnd();
        } else {
            if (countNewMessage == 0) {
                /**
                 * remove unread layout message if already exist in chat list
                 */
                for (int i = (mAdapter.getItemCount() - 1); i >= 0; i--) {
                    if (mAdapter.getItem(i) instanceof UnreadMessage) {
                        mAdapter.remove(i);
                    }
                }
                firstUnreadMessageInChat = realmRoomMessage;
            }
            countNewMessage++;
            llScrollNavigate.setVisibility(View.VISIBLE);
            txtNewUnreadMessage.setText(countNewMessage + "");
            txtNewUnreadMessage.setVisibility(View.VISIBLE);
        }
    }

    /**
     * check difference position to end of adapter
     *
     * @return true if lower than END_CHAT_LIMIT otherwise return false
     */
    private boolean isEnd() {
        if (addToView) {
            if (((recyclerView.getLayoutManager()) == null) || ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition() + END_CHAT_LIMIT > recyclerView.getAdapter().getItemCount()) {
                return true;
            }
        }
        return false;
        //return addToView && ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition() + END_CHAT_LIMIT > recyclerView.getAdapter().getItemCount();
    }

    /**
     * open fragment show image and show all image for this room
     */
    private void showImage(final StructMessageInfo messageInfo, View view) {

        if (!isAdded() || G.fragmentActivity.isFinishing()) {
            return;
        }

        // for gone app bar
        InputMethodManager imm = (InputMethodManager) G.fragmentActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

        long selectedFileToken = parseLong(messageInfo.messageID);

        FragmentShowImage fragment = FragmentShowImage.newInstance();
        Bundle bundle = new Bundle();
        bundle.putLong("RoomId", mRoomId);
        bundle.putLong("SelectedImage", selectedFileToken);
        fragment.setArguments(bundle);
        fragment.appBarLayout = appBarLayout;
        new HelperFragment(fragment).setReplace(false).load();
    }

    /**
     * scroll to bottom if unread not exits otherwise go to unread line
     * hint : just do in loaded message
     */
    private void scrollToEnd() {
        if (recyclerView == null || recyclerView.getAdapter() == null) return;
        if (recyclerView.getAdapter().getItemCount() < 2) {
            return;
        }

        recyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {

                try {
                    LinearLayoutManager llm = (LinearLayoutManager) recyclerView.getLayoutManager();

                    int lastPosition = llm.findLastVisibleItemPosition();
                    if (lastPosition + 30 > mAdapter.getItemCount()) {
                        recyclerView.smoothScrollToPosition(recyclerView.getAdapter().getItemCount() - 1);
                    } else {
                        recyclerView.scrollToPosition(recyclerView.getAdapter().getItemCount() - 1);
                    }
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
            }
        }, 300);
    }

    private void storingLastPosition() {
        try {
            if (recyclerView != null && mAdapter != null) {
                int firstVisiblePosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
                if (mAdapter.getItem(firstVisiblePosition) instanceof TimeItem || mAdapter.getItem(firstVisiblePosition) instanceof UnreadMessage) {
                    firstVisiblePosition++;
                }

                if (mAdapter.getItem(firstVisiblePosition) instanceof TimeItem || mAdapter.getItem(firstVisiblePosition) instanceof UnreadMessage) {
                    firstVisiblePosition++;
                }

                long lastScrolledMessageID = 0;

                if (firstVisiblePosition + 15 < mAdapter.getAdapterItemCount()) {
                    lastScrolledMessageID = parseLong(mAdapter.getItem(firstVisiblePosition).mMessage.messageID);
                }

                //+Realm realm = Realm.getDefaultInstance();

                final RealmRoom realmRoom = getRealmChat().where(RealmRoom.class).equalTo(RealmRoomFields.ID, mRoomId).findFirst();
                if (realmRoom != null) {
                    saveMessageIdPositionState(lastScrolledMessageID);
                }

                //realm.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {

        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(metrics);
        int height = metrics.heightPixels;
        int width = metrics.widthPixels;

        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT || G.twoPaneMode) {
            G.maxChatBox = Math.min(width, height) - ViewMaker.i_Dp(R.dimen.dp80);
        } else {
            G.maxChatBox = Math.max(width, height) - ViewMaker.i_Dp(R.dimen.dp80);
        }

        G.handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                updateShowItemInScreen();
            }
        }, 300);

        super.onConfigurationChanged(newConfig);
    }

    /**
     * save latest messageId position that user saw in chat before close it
     */
    private void saveMessageIdPositionState(final long position) {

        //+Realm realm = Realm.getDefaultInstance();

        getRealmChat().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmRoom realmRoom = realm.where(RealmRoom.class).equalTo(RealmRoomFields.ID, mRoomId).findFirst();
                if (realmRoom != null) {
                    realmRoom.setLastScrollPositionMessageId(position);
                }
            }
        });

        //realm.close();
    }

    /**
     * get images for show in bottom sheet
     */
    public static ArrayList<StructBottomSheet> getAllShownImagesPath(Activity activity) {
        Uri uri;
        Cursor cursor;
        int column_index_data = 0, column_index_folder_name;
        ArrayList<StructBottomSheet> listOfAllImages = new ArrayList<StructBottomSheet>();
        String absolutePathOfImage = null;
        uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String[] projection = {
                MediaStore.MediaColumns.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME
        };

        cursor = activity.getContentResolver().query(uri, projection, null, null, null);

        if (cursor != null) {
            column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);

            column_index_folder_name = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);

            while (cursor.moveToNext()) {
                absolutePathOfImage = cursor.getString(column_index_data);

                StructBottomSheet item = new StructBottomSheet();
                item.setPath(absolutePathOfImage);
                item.isSelected = true;
                listOfAllImages.add(0, item);
            }
            cursor.close();
        }

        return listOfAllImages;
    }

    /**
     * emoji initialization
     */
    private void setUpEmojiPopup() {
        emojiPopup = EmojiPopup.Builder.fromRootView(rootView.findViewById(ac_ll_parent)).setOnEmojiBackspaceClickListener(new OnEmojiBackspaceClickListener() {

            @Override
            public void onEmojiBackspaceClick(View v) {

            }
        }).setOnEmojiPopupShownListener(new OnEmojiPopupShownListener() {
            @Override
            public void onEmojiPopupShown() {
                changeEmojiButtonImageResource(R.string.md_black_keyboard_with_white_keys);
                isEmojiSHow = true;
            }
        }).setOnSoftKeyboardOpenListener(new OnSoftKeyboardOpenListener() {
            @Override
            public void onKeyboardOpen(final int keyBoardHeight) {

            }
        }).setOnEmojiPopupDismissListener(new OnEmojiPopupDismissListener() {
            @Override
            public void onEmojiPopupDismiss() {
                changeEmojiButtonImageResource(R.string.md_emoticon_with_happy_face);
                isEmojiSHow = false;
            }
        }).setOnSoftKeyboardCloseListener(new OnSoftKeyboardCloseListener() {
            @Override
            public void onKeyboardClose() {
                emojiPopup.dismiss();
            }
        }).build(edtChat);
    }

    private void changeEmojiButtonImageResource(@StringRes int drawableResourceId) {
        imvSmileButton.setText(drawableResourceId);
    }

    /**
     * *************************** draft ***************************
     */
    private void setDraftMessage(final int requestCode) {

        G.handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (listPathString == null) return;
                if (listPathString.size() < 1) return;
                if (listPathString.get(0) == null) return;
                String filename = listPathString.get(0).substring(listPathString.get(0).lastIndexOf("/") + 1);
                switch (requestCode) {
                    case AttachFile.request_code_TAKE_PICTURE:
                        txtFileNameForSend.setText(G.context.getResources().getString(R.string.image_selected_for_send) + "\n" + filename);
                        break;
                    case AttachFile.requestOpenGalleryForImageMultipleSelect:
                        if (listPathString.size() == 1) {
                            if (!listPathString.get(0).toLowerCase().endsWith(".gif")) {
                                txtFileNameForSend.setText(G.context.getResources().getString(R.string.image_selected_for_send) + "\n" + filename);
                            } else {
                                txtFileNameForSend.setText(G.context.getResources().getString(R.string.gif_selected_for_send) + "\n" + filename);
                            }
                        } else {
                            txtFileNameForSend.setText(listPathString.size() + G.context.getResources().getString(R.string.image_selected_for_send) + "\n" + filename);
                        }

                        break;

                    case AttachFile.requestOpenGalleryForVideoMultipleSelect:
                        txtFileNameForSend.setText(G.context.getResources().getString(R.string.multi_video_selected_for_send) + "\n" + filename);
                        break;
                    case request_code_VIDEO_CAPTURED:

                        if (listPathString.size() == 1) {
                            txtFileNameForSend.setText(G.context.getResources().getString(R.string.video_selected_for_send));
                        } else {
                            txtFileNameForSend.setText(listPathString.size() + G.context.getResources().getString(R.string.video_selected_for_send) + "\n" + filename);
                        }
                        break;

                    case AttachFile.request_code_pic_audi:
                        if (listPathString.size() == 1) {
                            txtFileNameForSend.setText(G.context.getResources().getString(R.string.audio_selected_for_send) + "\n" + filename);
                        } else {
                            txtFileNameForSend.setText(listPathString.size() + G.context.getResources().getString(R.string.audio_selected_for_send) + "\n" + filename);
                        }
                        break;
                    case AttachFile.request_code_pic_file:
                        txtFileNameForSend.setText(G.context.getResources().getString(R.string.file_selected_for_send) + "\n" + filename);
                        break;
                    case AttachFile.request_code_open_document:
                        if (listPathString.size() == 1) {
                            txtFileNameForSend.setText(G.context.getResources().getString(R.string.file_selected_for_send) + "\n" + filename);
                        }
                        break;
                    case AttachFile.request_code_paint:
                        if (listPathString.size() == 1) {
                            txtFileNameForSend.setText(G.context.getResources().getString(R.string.pain_selected_for_send) + "\n" + filename);
                        }
                        break;
                    case AttachFile.request_code_contact_phone:
                        txtFileNameForSend.setText(G.context.getResources().getString(R.string.phone_selected_for_send) + "\n" + filename);
                        break;
                    case IntentRequests.REQ_CROP:
                        if (!listPathString.get(0).toLowerCase().endsWith(".gif")) {
                            txtFileNameForSend.setText(G.context.getResources().getString(R.string.crop_selected_for_send) + "\n" + filename);
                        } else {
                            txtFileNameForSend.setText(G.context.getResources().getString(R.string.gif_selected_for_send) + "\n" + filename);
                        }
                        break;
                }
            }
        }, 100);
    }

    private void showDraftLayout() {
        /**
         * onActivityResult happens before onResume, so Presenter does not have View attached. because use handler
         */
        G.handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (listPathString == null) return;
                if (listPathString.size() < 1) return;
                if (listPathString.get(0) == null) return;
                if (ll_attach_text == null) { // have null error , so reInitialize for avoid that

                    ll_attach_text = (LinearLayout) rootView.findViewById(R.id.ac_ll_attach_text);
                    layoutAttachBottom = (LinearLayout) rootView.findViewById(R.id.layoutAttachBottom);
                    imvSendButton = (MaterialDesignTextView) rootView.findViewById(R.id.chl_imv_send_button);
                }

                ll_attach_text.setVisibility(View.VISIBLE);
                // set maxLength  when layout attachment is visible
                edtChat.setFilters(new InputFilter[]{new InputFilter.LengthFilter(Config.MAX_TEXT_ATTACHMENT_LENGTH)});

                layoutAttachBottom.animate().alpha(0F).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        layoutAttachBottom.setVisibility(View.GONE);
                    }
                }).start();
                imvSendButton.animate().alpha(1F).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        G.handler.post(new Runnable() {
                            @Override
                            public void run() {
                                imvSendButton.clearAnimation();
                                imvSendButton.setVisibility(View.VISIBLE);
                            }
                        });
                    }
                }).start();
            }
        }, 100);
    }

    private void setDraft() {
        if (!isNotJoin) {
            //Realm realm = Realm.getDefaultInstance();

            final RealmRoom realmRoom = getRealmChat().where(RealmRoom.class).equalTo(RealmRoomFields.ID, mRoomId).findFirst();
            if (realmRoom == null) {
                return;
            }

            if (mReplayLayout != null && mReplayLayout.getVisibility() == View.VISIBLE) {
                StructMessageInfo info = ((StructMessageInfo) mReplayLayout.getTag());
                if (info != null) {
                    replyToMessageId = parseLong(info.messageID);
                }
            } else {
                replyToMessageId = 0;
            }
            if (edtChat == null) {
                return;
            }

            String message = edtChat.getText().toString();

            if (!message.trim().isEmpty() || ((mReplayLayout != null && mReplayLayout.getVisibility() == View.VISIBLE))) {

                hasDraft = true;

                final String finalMessage = message;
                getRealmChat().executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {

                        RealmRoomDraft draft = realm.createObject(RealmRoomDraft.class);
                        draft.setMessage(finalMessage);
                        draft.setReplyToMessageId(replyToMessageId);

                        realmRoom.setDraft(draft);

                        if (chatType == CHAT) {
                            new RequestChatUpdateDraft().chatUpdateDraft(mRoomId, finalMessage, replyToMessageId);
                        } else if (chatType == GROUP) {
                            new RequestGroupUpdateDraft().groupUpdateDraft(mRoomId, finalMessage, replyToMessageId);
                        } else if (chatType == CHANNEL) {
                            new RequestChannelUpdateDraft().channelUpdateDraft(mRoomId, finalMessage, replyToMessageId);
                        }
                        if (G.onDraftMessage != null) {
                            G.onDraftMessage.onDraftMessage(mRoomId, finalMessage);
                        }
                    }
                });
            } else {
                clearDraftRequest();
            }
            //realm.close();
        }
    }

    private void getDraft() {
        //+Realm realm = Realm.getDefaultInstance();
        RealmRoom realmRoom = getRealmChat().where(RealmRoom.class).equalTo(RealmRoomFields.ID, mRoomId).findFirst();
        if (realmRoom != null) {
            RealmRoomDraft draft = realmRoom.getDraft();
            if (draft != null) {
                hasDraft = true;
                edtChat.setText(draft.getMessage());
            }
        }
        //realm.close();
        clearLocalDraft();
    }

    private void clearLocalDraft() {
        //+Realm realm = Realm.getDefaultInstance();
        getRealmChat().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmRoom realmRoom = realm.where(RealmRoom.class).equalTo(RealmRoomFields.ID, mRoomId).findFirst();
                if (realmRoom != null) {
                    realmRoom.setDraft(null);
                    realmRoom.setDraftFile(null);
                }
            }
        });
        //realm.close();
    }

    private void clearDraftRequest() {
        if (hasDraft) {
            hasDraft = false;
            if (chatType == CHAT) {
                new RequestChatUpdateDraft().chatUpdateDraft(mRoomId, "", 0);
            } else if (chatType == GROUP) {
                new RequestGroupUpdateDraft().groupUpdateDraft(mRoomId, "", 0);
            } else if (chatType == CHANNEL) {
                new RequestChannelUpdateDraft().channelUpdateDraft(mRoomId, "", 0);
            }

            clearLocalDraft();
        }
    }

    /**
     * *************************** sheared data ***************************
     */



    private void insertShearedData() {
        /**
         * run this method with delay , because client get local message with delay
         * for show messages with async state and before run getLocalMessage this shared
         * item added to realm and view, and after that getLocalMessage called and new item
         * got from realm and add to view again but in this time from getLocalMessage method
         */
        G.handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (HelperGetDataFromOtherApp.hasSharedData) {
                    HelperGetDataFromOtherApp.hasSharedData = false;

                    ArrayList<String> pathList = new ArrayList<String>();

                    if (messageType != HelperGetDataFromOtherApp.FileType.message) {
                        if (HelperGetDataFromOtherApp.messageFileAddress.size() == 1) {

                            Uri _Uri = HelperGetDataFromOtherApp.messageFileAddress.get(0);
                            String _path = getFilePathFromUri(Uri.parse(_Uri.toString()));
                            if (_path == null) {
                                _path = getPathN(_Uri, messageType);
                            }
                            pathList.add(_path);
                        } else {

                            for (int i = 0; i < HelperGetDataFromOtherApp.messageFileAddress.size(); i++) {

                                Uri _Uri = HelperGetDataFromOtherApp.messageFileAddress.get(i);
                                String _path = getFilePathFromUri(Uri.parse(_Uri.toString()));

                                if (_path == null) {
                                    _path = getPathN(_Uri, HelperGetDataFromOtherApp.fileTypeArray.get(i));
                                }
                                pathList.add(_path);
                            }
                        }
                    }

                    if (messageType == HelperGetDataFromOtherApp.FileType.message) {
                        String message = HelperGetDataFromOtherApp.message;
                        edtChat.setText(message);
                        imvSendButton.performClick();
                    } else if (messageType == HelperGetDataFromOtherApp.FileType.image) {

                        for (int i = 0; i < pathList.size(); i++) {
                            sendMessage(AttachFile.request_code_TAKE_PICTURE, pathList.get(i));
                        }
                    } else if (messageType == HelperGetDataFromOtherApp.FileType.video) {
                        if (pathList.size() == 1 && (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && (sharedPreferences.getInt(SHP_SETTING.KEY_COMPRESS, 1) == 1))) {
                            final String savePathVideoCompress = Environment.getExternalStorageDirectory() + File.separator + com.lalongooo.videocompressor.Config.VIDEO_COMPRESSOR_APPLICATION_DIR_NAME + com.lalongooo.videocompressor.Config.VIDEO_COMPRESSOR_COMPRESSED_VIDEOS_DIR + "VIDEO_" + new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date()) + ".mp4";
                            mainVideoPath = pathList.get(0);

                            if (mainVideoPath == null) {
                                return;
                            }

                            G.handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    new VideoCompressor().execute(mainVideoPath, savePathVideoCompress);
                                }
                            }, 200);
                            sendMessage(request_code_VIDEO_CAPTURED, savePathVideoCompress);
                        } else {
                            for (int i = 0; i < pathList.size(); i++) {
                                compressedPath.put(pathList.get(i), true);
                                sendMessage(request_code_VIDEO_CAPTURED, pathList.get(i));
                            }
                        }
                    } else if (messageType == HelperGetDataFromOtherApp.FileType.audio) {

                        for (int i = 0; i < pathList.size(); i++) {
                            sendMessage(AttachFile.request_code_pic_audi, pathList.get(i));
                        }
                    } else if (messageType == HelperGetDataFromOtherApp.FileType.file) {

                        for (int i = 0; i < pathList.size(); i++) {
                            HelperGetDataFromOtherApp.FileType fileType = messageType = HelperGetDataFromOtherApp.FileType.file;
                            if (HelperGetDataFromOtherApp.fileTypeArray.size() > 0) {
                                fileType = HelperGetDataFromOtherApp.fileTypeArray.get(i);
                            }

                            if (fileType == HelperGetDataFromOtherApp.FileType.image) {
                                sendMessage(AttachFile.request_code_TAKE_PICTURE, pathList.get(i));
                            } else if (fileType == HelperGetDataFromOtherApp.FileType.video) {
                                if (pathList.size() == 1 && (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && (sharedPreferences.getInt(SHP_SETTING.KEY_COMPRESS, 1) == 1))) {
                                    final String savePathVideoCompress = Environment.getExternalStorageDirectory() + File.separator + com.lalongooo.videocompressor.Config.VIDEO_COMPRESSOR_APPLICATION_DIR_NAME + com.lalongooo.videocompressor.Config.VIDEO_COMPRESSOR_COMPRESSED_VIDEOS_DIR + "VIDEO_" + new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date()) + ".mp4";
                                    mainVideoPath = pathList.get(0);

                                    G.handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            new VideoCompressor().execute(mainVideoPath, savePathVideoCompress);
                                        }
                                    }, 200);
                                    sendMessage(request_code_VIDEO_CAPTURED, savePathVideoCompress);
                                } else {
                                    compressedPath.put(pathList.get(i), true);
                                    sendMessage(request_code_VIDEO_CAPTURED, pathList.get(i));
                                }
                            } else if (fileType == HelperGetDataFromOtherApp.FileType.audio) {
                                sendMessage(AttachFile.request_code_pic_audi, pathList.get(i));
                            } else if (fileType == HelperGetDataFromOtherApp.FileType.file) {
                                sendMessage(AttachFile.request_code_open_document, pathList.get(i));
                            }
                        }
                    }
                    messageType = null;
                }
            }
        }, 300);
    }

    private void shearedDataToOtherProgram(StructMessageInfo messageInfo) {

        if (messageInfo == null) return;

        try {
            Intent intent = new Intent(Intent.ACTION_SEND);
            String chooserDialogText = "";

            ProtoGlobal.RoomMessageType type = messageInfo.forwardedFrom != null ? messageInfo.forwardedFrom.getMessageType() : messageInfo.messageType;

            switch (type.toString()) {

                case "TEXT":
                    intent.setType("text/plain");
                    String message = messageInfo.forwardedFrom != null ? messageInfo.forwardedFrom.getMessage() : messageInfo.messageText;
                    intent.putExtra(Intent.EXTRA_TEXT, message);
                    break;
                case "CONTACT":
                    intent.setType("text/plain");
                    String messageContact;
                    if (messageInfo.forwardedFrom != null) {
                        messageContact = messageInfo.forwardedFrom.getRoomMessageContact().getFirstName() + " " + messageInfo.forwardedFrom.getRoomMessageContact().getLastName() + "\n" + messageInfo.forwardedFrom.getRoomMessageContact().getLastPhoneNumber();
                    } else {
                        messageContact = messageInfo.userInfo.firstName + "\n" + messageInfo.userInfo.phone;
                    }
                    intent.putExtra(Intent.EXTRA_TEXT, messageContact);
                    break;
                case "LOCATION":
                    String imagePathPosition = messageInfo.forwardedFrom != null ? messageInfo.forwardedFrom.getLocation().getImagePath() : messageInfo.location.getImagePath();
                    intent.setType("image/*");
                    if (imagePathPosition != null) {
                        intent.putExtra(Intent.EXTRA_STREAM, AppUtils.createtUri(new File(imagePathPosition)));
                    }
                    break;
                case "VOICE":
                case "AUDIO":
                case "AUDIO_TEXT":
                    intent.setType("audio/*");
                    putExtra(intent, messageInfo);
                    chooserDialogText = G.context.getResources().getString(R.string.share_audio_file);
                    break;
                case "IMAGE":
                case "IMAGE_TEXT":
                    intent.setType("image/*");
                    putExtra(intent, messageInfo);
                    chooserDialogText = G.context.getResources().getString(R.string.share_image);
                    break;
                case "VIDEO":
                case "VIDEO_TEXT":
                    intent.setType("video/*");
                    putExtra(intent, messageInfo);
                    chooserDialogText = G.context.getResources().getString(R.string.share_video_file);
                    break;
                case "FILE":
                case "FILE_TEXT":
                    String mfilepath = messageInfo.forwardedFrom != null ? messageInfo.forwardedFrom.getAttachment().getLocalFilePath() : messageInfo.attachment.getLocalFilePath();
                    if (mfilepath != null) {
                        Uri uri = AppUtils.createtUri(new File(mfilepath));

                        ContentResolver cR = context.getContentResolver();
                        MimeTypeMap mime = MimeTypeMap.getSingleton();
                        String mimeType = mime.getExtensionFromMimeType(cR.getType(uri));

                        if (mimeType == null || mimeType.length() < 1) {
                            mimeType = "*/*";
                        }

                        intent.setType(mimeType);
                        intent.putExtra(Intent.EXTRA_STREAM, uri);
                        chooserDialogText = G.context.getResources().getString(R.string.share_file);
                    } else {
                        G.handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context, R.string.file_not_download_yet, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    break;
            }

            startActivity(Intent.createChooser(intent, chooserDialogText));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * *************************** init layout ***************************
     */

    /**
     * init layout for hashtak up and down
     */
    private void initLayoutHashNavigationCallback() {

        hashListener = new OnComplete() {
            @Override
            public void complete(boolean result, String text, String messageId) {

                if (!initHash) {
                    initHash = true;
                    initHashView();
                }

                searchHash.setHashString(text);
                searchHash.setPosition(messageId);
                ll_navigateHash.setVisibility(View.VISIBLE);
                viewAttachFile.setVisibility(View.GONE);

                if (chatType == CHANNEL && channelRole == ChannelChatRole.MEMBER) {
                    rootView.findViewById(R.id.chl_ll_channel_footer).setVisibility(View.GONE);
                }
            }
        };
    }

    /**
     * init layout hashtak for up and down
     */
    private void initHashView() {
        ll_navigateHash = (LinearLayout) rootView.findViewById(R.id.ac_ll_hash_navigation);
        btnUpHash = (TextView) rootView.findViewById(R.id.ac_btn_hash_up);
        btnDownHash = (TextView) rootView.findViewById(R.id.ac_btn_hash_down);
        txtHashCounter = (TextView) rootView.findViewById(R.id.ac_txt_hash_counter);

        btnUpHash.setTextColor(Color.parseColor(G.appBarColor));
        btnDownHash.setTextColor(Color.parseColor(G.appBarColor));
        txtHashCounter.setTextColor(Color.parseColor(G.appBarColor));

        searchHash = new SearchHash();

        btnHashLayoutClose = (MaterialDesignTextView) rootView.findViewById(R.id.ac_btn_hash_close);
        btnHashLayoutClose.setTextColor(Color.parseColor(G.appBarColor));
        btnHashLayoutClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ll_navigateHash.setVisibility(View.GONE);

                mAdapter.toggleSelection(searchHash.lastMessageId, false, null);

                if (chatType == CHANNEL && channelRole == ChannelChatRole.MEMBER) {
                    rootView.findViewById(R.id.chl_ll_channel_footer).setVisibility(View.VISIBLE);
                } else {
                    viewAttachFile.setVisibility(View.VISIBLE);
                }
            }
        });

        btnUpHash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                searchHash.upHash();
            }
        });

        btnDownHash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchHash.downHash();
            }
        });
    }

    /**
     * manage need showSpamBar for user or no
     */
    private void showSpamBar() {
        /**
         * use handler for run async
         */
        G.handler.post(new Runnable() {
            @Override
            public void run() {

                //+Realm realm = Realm.getDefaultInstance();
                RealmRegisteredInfo realmRegisteredInfo = getRealmChat().where(RealmRegisteredInfo.class).equalTo(RealmRegisteredInfoFields.ID, chatPeerId).findFirst();
                RealmContacts realmContacts = getRealmChat().where(RealmContacts.class).equalTo(RealmContactsFields.ID, chatPeerId).findFirst();
                if (realmRegisteredInfo != null && realmRegisteredInfo.getId() != G.userId) {
                    if (phoneNumber == null) {
                        if (realmContacts == null && chatType == CHAT && !isChatReadOnly) {
                            initSpamBarLayout(realmRegisteredInfo);
                            vgSpamUser.setVisibility(View.VISIBLE);
                        }
                    }

                    if (realmRegisteredInfo.getId() != G.userId) {
                        if (!realmRegisteredInfo.getDoNotshowSpamBar()) {

                            if (realmRegisteredInfo.isBlockUser()) {
                                initSpamBarLayout(realmRegisteredInfo);
                                blockUser = true;
                                txtSpamUser.setText(G.context.getResources().getString(R.string.un_block_user));
                                vgSpamUser.setVisibility(View.VISIBLE);
                            } else {
                                if (vgSpamUser != null) {
                                    vgSpamUser.setVisibility(View.GONE);
                                }
                            }
                        }
                    }

                    if (realmContacts != null && realmRegisteredInfo.getId() != G.userId) {
                        if (realmContacts.isBlockUser()) {
                            if (!realmRegisteredInfo.getDoNotshowSpamBar()) {
                                initSpamBarLayout(realmRegisteredInfo);
                                blockUser = true;
                                txtSpamUser.setText(G.context.getResources().getString(R.string.un_block_user));
                                vgSpamUser.setVisibility(View.VISIBLE);
                            } else {
                                initSpamBarLayout(realmRegisteredInfo);
                                blockUser = true;
                                txtSpamUser.setText(G.context.getResources().getString(R.string.un_block_user));
                                vgSpamUser.setVisibility(View.VISIBLE);
                            }
                        } else {
                            if (vgSpamUser != null) {
                                vgSpamUser.setVisibility(View.GONE);
                            }
                        }
                    }
                }
                //realm.close();
            }
        });
    }

    /**
     * init spamBar layout
     */
    private void initSpamBarLayout(final RealmRegisteredInfo registeredInfo) {
        vgSpamUser = (ViewGroup) rootView.findViewById(R.id.layout_add_contact);
        txtSpamUser = (TextView) rootView.findViewById(R.id.chat_txt_addContact);
        txtSpamClose = (TextView) rootView.findViewById(R.id.chat_txt_close);
        txtSpamClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vgSpamUser.setVisibility(View.GONE);
                if (registeredInfo != null) {

                    //+Realm realm = Realm.getDefaultInstance();

                    getRealmChat().executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            registeredInfo.setDoNotshowSpamBar(true);
                        }
                    });

                    //realm.close();
                }
            }
        });

        txtSpamUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (blockUser) {
                    G.onUserContactsUnBlock = new OnUserContactsUnBlock() {
                        @Override
                        public void onUserContactsUnBlock(final long userId) {
                            G.handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    blockUser = false;
                                    if (userId == chatPeerId) {
                                        txtSpamUser.setText(G.context.getResources().getString(R.string.block_user));
                                    }
                                }
                            });
                        }
                    };

                    new MaterialDialog.Builder(G.fragmentActivity).title(R.string.unblock_the_user).content(R.string.unblock_the_user_text).positiveText(R.string.ok).onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            new RequestUserContactsUnblock().userContactsUnblock(chatPeerId);
                        }
                    }).negativeText(R.string.cancel).show();
                } else {

                    G.onUserContactsBlock = new OnUserContactsBlock() {
                        @Override
                        public void onUserContactsBlock(final long userId) {
                            G.handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    blockUser = true;
                                    if (userId == chatPeerId) {
                                        txtSpamUser.setText(G.context.getResources().getString(R.string.un_block_user));
                                    }
                                }
                            });
                        }
                    };

                    new MaterialDialog.Builder(G.fragmentActivity).title(R.string.block_the_user).content(R.string.block_the_user_text).positiveText(R.string.ok).onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            new RequestUserContactsBlock().userContactsBlock(chatPeerId);
                        }
                    }).negativeText(R.string.cancel).show();

                }
            }
        });
    }

    /**
     * initialize bottomSheet for use in attachment
     */
    private void initAttach() {

        fastItemAdapter = new FastItemAdapter();

        viewBottomSheet = G.fragmentActivity.getLayoutInflater().inflate(R.layout.bottom_sheet, null);

        TextView txtCamera = (TextView) viewBottomSheet.findViewById(R.id.txtCamera);
        TextView textPicture = (TextView) viewBottomSheet.findViewById(R.id.textPicture);
        TextView txtVideo = (TextView) viewBottomSheet.findViewById(R.id.txtVideo);
        TextView txtMusic = (TextView) viewBottomSheet.findViewById(R.id.txtMusic);
        TextView txtDocument = (TextView) viewBottomSheet.findViewById(R.id.txtDocument);
        TextView txtFile = (TextView) viewBottomSheet.findViewById(R.id.txtFile);
        TextView txtPaint = (TextView) viewBottomSheet.findViewById(R.id.txtPaint);
        TextView txtLocation = (TextView) viewBottomSheet.findViewById(R.id.txtLocation);
        TextView txtContact = (TextView) viewBottomSheet.findViewById(R.id.txtContact);
        send = (TextView) viewBottomSheet.findViewById(R.id.txtSend);

        txtCamera.setTextColor(Color.parseColor(G.attachmentColor));
        textPicture.setTextColor(Color.parseColor(G.attachmentColor));
        txtVideo.setTextColor(Color.parseColor(G.attachmentColor));
        txtMusic.setTextColor(Color.parseColor(G.attachmentColor));
        txtDocument.setTextColor(Color.parseColor(G.attachmentColor));
        txtFile.setTextColor(Color.parseColor(G.attachmentColor));
        txtPaint.setTextColor(Color.parseColor(G.attachmentColor));
        txtLocation.setTextColor(Color.parseColor(G.attachmentColor));
        txtContact.setTextColor(Color.parseColor(G.attachmentColor));
        send.setTextColor(Color.parseColor(G.attachmentColor));

        txtCountItem = (TextView) viewBottomSheet.findViewById(R.id.txtNumberItem);
        ViewGroup camera = (ViewGroup) viewBottomSheet.findViewById(R.id.camera);
        ViewGroup picture = (ViewGroup) viewBottomSheet.findViewById(R.id.picture);
        ViewGroup video = (ViewGroup) viewBottomSheet.findViewById(R.id.video);
        ViewGroup music = (ViewGroup) viewBottomSheet.findViewById(R.id.music);
        ViewGroup document = (ViewGroup) viewBottomSheet.findViewById(R.id.document);
        ViewGroup close = (ViewGroup) viewBottomSheet.findViewById(R.id.close);
        ViewGroup file = (ViewGroup) viewBottomSheet.findViewById(R.id.file);
        ViewGroup paint = (ViewGroup) viewBottomSheet.findViewById(R.id.paint);
        ViewGroup location = (ViewGroup) viewBottomSheet.findViewById(R.id.location);
        ViewGroup contact = (ViewGroup) viewBottomSheet.findViewById(R.id.contact);

        onPathAdapterBottomSheet = new OnPathAdapterBottomSheet() {
            @Override
            public void path(String path, boolean isCheck) {

                if (isCheck) {
                    listPathString.add(path);
                } else {
                    listPathString.remove(path);
                }

                listPathString.size();
                if (listPathString.size() > 0) {
                    //send.setText(R.mipmap.send2);
                    send.setText(G.context.getResources().getString(R.string.md_send_button));
                    isCheckBottomSheet = true;
                    txtCountItem.setText("" + listPathString.size() + " " + G.context.getResources().getString(item));
                } else {
                    //send.setImageResource(R.mipmap.ic_close);
                    send.setText(G.context.getResources().getString(R.string.igap_chevron_double_down));
                    isCheckBottomSheet = false;
                    txtCountItem.setText(G.context.getResources().getString(R.string.navigation_drawer_close));
                }
            }
        };

        rcvBottomSheet = (RecyclerView) viewBottomSheet.findViewById(R.id.rcvContent);
        rcvBottomSheet.setLayoutManager(new GridLayoutManager(G.fragmentActivity, 1, GridLayoutManager.HORIZONTAL, false));
        rcvBottomSheet.setItemViewCacheSize(100);
        rcvBottomSheet.setAdapter(fastItemAdapter);
        bottomSheetDialog = new BottomSheetDialog(G.fragmentActivity);
        bottomSheetDialog.setContentView(viewBottomSheet);
        final BottomSheetBehavior mBehavior = BottomSheetBehavior.from((View) viewBottomSheet.getParent());

        viewBottomSheet.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    mBehavior.setPeekHeight(viewBottomSheet.getHeight());
                    viewBottomSheet.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            }
        });
        //height is ready

        bottomSheetDialog.show();
        onClickCamera = new OnClickCamera() {
            @Override
            public void onclickCamera() {
                try {
                    bottomSheetDialog.dismiss();
                    new AttachFile(G.fragmentActivity).requestTakePicture(FragmentChat.this);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        bottomSheetDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) dialogInterface;
                FrameLayout bottomSheet = (FrameLayout) bottomSheetDialog.findViewById(android.support.design.R.id.design_bottom_sheet);
                BottomSheetBehavior.from(bottomSheet).setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });

        rcvBottomSheet.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(final View view) {
                if (isPermissionCamera) {

                    if (rcvBottomSheet.getChildAdapterPosition(view) == 0) {
                        isCameraAttached = true;
                    }
                    if (isCameraAttached) {
                        if (fotoapparatSwitcher != null) {
                            if (!isCameraStart) {
                                isCameraStart = true;
                                try {
                                    G.handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            fotoapparatSwitcher.start();
                                        }
                                    }, 50);
                                } catch (Exception e) {
                                    e.getMessage();
                                }
                            }
                        } else {
                            if (!isCameraStart) {
                                isCameraStart = true;
                                try {
                                    G.handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {

                                            fotoapparatSwitcher = Fotoapparat.with(G.fragmentActivity).into((CameraRenderer) view.findViewById(R.id.cameraView))           // view which will draw the camera preview
                                                    .photoSize(biggestSize())   // we want to have the biggest photo possible
                                                    .lensPosition(back())       // we want back camera
                                                    .build();

                                            fotoapparatSwitcher.start();
                                        }
                                    }, 100);
                                } catch (IllegalStateException e) {
                                    e.getMessage();
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void onChildViewDetachedFromWindow(final View view) {

                if (isPermissionCamera) {
                    if (rcvBottomSheet.getChildAdapterPosition(view) == 0) {
                        isCameraAttached = false;
                    }
                    if (!isCameraAttached) {
                        if (fotoapparatSwitcher != null) {
                            //                    if (isCameraStart && ( rcvBottomSheet.getChildAdapterPosition(view)> 4  || rcvBottomSheet.computeHorizontalScrollOffset() >200)){
                            if (isCameraStart) {

                                try {
                                    fotoapparatSwitcher.stop();
                                    isCameraStart = false;
                                } catch (Exception e) {
                                    e.getMessage();
                                }
                            }
                        } else {
                            if (!isCameraStart) {
                                isCameraStart = false;
                                try {
                                    G.handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            fotoapparatSwitcher = Fotoapparat.with(G.fragmentActivity).into((CameraRenderer) view.findViewById(R.id.cameraView))           // view which will draw the camera preview
                                                    .photoSize(biggestSize())   // we want to have the biggest photo possible
                                                    .lensPosition(back())       // we want back camera
                                                    .build();

                                            fotoapparatSwitcher.stop();
                                        }
                                    }, 100);
                                } catch (IllegalStateException e) {
                                    e.getMessage();
                                }
                            }
                        }
                    }
                }
            }
        });

        rcvBottomSheet.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(final View v) {
                if (isPermissionCamera) {

                    if (fotoapparatSwitcher != null) {
                        G.handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (!isCameraStart) {
                                    fotoapparatSwitcher.start();
                                    isCameraStart = true;
                                }
                            }
                        }, 50);
                    }
                }
            }

            @Override
            public void onViewDetachedFromWindow(View v) {
                if (isPermissionCamera) {
                    if (fotoapparatSwitcher != null) {
                        if (isCameraStart) {
                            fotoapparatSwitcher.stop();
                            isCameraStart = false;
                        }
                    }
                }
            }
        });

        bottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                dialog.dismiss();
                //send.setImageResource(R.mipmap.ic_close);
                send.setText(G.context.getResources().getString(R.string.igap_chevron_double_down));
                txtCountItem.setText(G.context.getResources().getString(R.string.navigation_drawer_close));
                itemGalleryList.clear();
            }
        });

        listPathString = new ArrayList<>();

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();

                if (sharedPreferences.getInt(SHP_SETTING.KEY_CROP, 1) == 1) {
                    attachFile.showDialogOpenCamera(toolbar, null, FragmentChat.this);
                } else {
                    attachFile.showDialogOpenCamera(toolbar, null, FragmentChat.this);
                }
            }
        });
        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                try {
                    attachFile.requestOpenGalleryForImageMultipleSelect(FragmentChat.this);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                try {
                    attachFile.requestOpenGalleryForVideoMultipleSelect(FragmentChat.this);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                try {
                    attachFile.requestPickAudio(FragmentChat.this);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        document.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bottomSheetDialog.dismiss();
                try {
                    attachFile.requestOpenDocumentFolder(new IPickFile() {
                        @Override
                        public void onPick(ArrayList<String> selectedPathList) {

                            for (String path : selectedPathList) {
                                Intent data = new Intent();
                                data.setData(Uri.parse(path));
                                onActivityResult(request_code_open_document, Activity.RESULT_OK, data);
                            }
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isCheckBottomSheet) {
                    bottomSheetDialog.dismiss();

                    fastItemAdapter.clear();
                    //send.setImageResource(R.mipmap.ic_close);
                    send.setText(G.context.getResources().getString(R.string.igap_chevron_double_down));
                    txtCountItem.setText(G.context.getResources().getString(R.string.navigation_drawer_close));

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            G.handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    ArrayList<String> pathStrings = listPathString;
                                    for (String path : pathStrings) {
                                        //if (!path.toLowerCase().endsWith(".gif")) {
                                        String localPathNew = attachFile.saveGalleryPicToLocal(path);
                                        sendMessage(AttachFile.requestOpenGalleryForImageMultipleSelect, localPathNew);
                                        //}
                                    }
                                }
                            });
                        }
                    }).start();
                } else {
                    bottomSheetDialog.dismiss();
                }
            }
        });
        file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                try {
                    attachFile.requestPickFile(new IPickFile() {
                        @Override
                        public void onPick(ArrayList<String> selectedPathList) {
                            for (String path : selectedPathList) {
                                Intent data = new Intent();
                                data.setData(Uri.parse(path));
                                onActivityResult(request_code_pic_file, Activity.RESULT_OK, data);
                            }
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        paint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                try {
                    attachFile.requestPaint(FragmentChat.this);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                try {
                    attachFile.requestGetPosition(complete, FragmentChat.this);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                try {
                    attachFile.requestPickContact(FragmentChat.this);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void inflateReplayLayoutIntoStub(StructMessageInfo chatItem) {
        if (rootView.findViewById(R.id.replayLayoutAboveEditText) == null) {
            ViewStubCompat stubView = (ViewStubCompat) rootView.findViewById(R.id.replayLayoutStub);
            stubView.setInflatedId(R.id.replayLayoutAboveEditText);
            stubView.setLayoutResource(R.layout.layout_chat_reply);
            stubView.inflate();

            inflateReplayLayoutIntoStub(chatItem);
        } else {
            mReplayLayout = (LinearLayout) rootView.findViewById(R.id.replayLayoutAboveEditText);
            mReplayLayout.setVisibility(View.VISIBLE);
            TextView replayTo = (TextView) mReplayLayout.findViewById(R.id.replayTo);
            replayTo.setTypeface(G.typeface_IRANSansMobile);
            TextView replayFrom = (TextView) mReplayLayout.findViewById(R.id.replyFrom);
            replayFrom.setTypeface(G.typeface_IRANSansMobile);
            replayFrom.setTextColor(Color.parseColor(G.appBarColor));

            ImageView imvReplayIcon = (ImageView) rootView.findViewById(R.id.lcr_imv_replay);
            imvReplayIcon.setColorFilter(Color.parseColor(G.appBarColor));

            ImageView thumbnail = (ImageView) mReplayLayout.findViewById(R.id.thumbnail);
            TextView closeReplay = (TextView) mReplayLayout.findViewById(R.id.cancelIcon);
            closeReplay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clearReplyView();
                }
            });
            //+Realm realm = Realm.getDefaultInstance();
            thumbnail.setVisibility(View.VISIBLE);
            if (chatItem.forwardedFrom != null) {
                AppUtils.rightFileThumbnailIcon(thumbnail, chatItem.forwardedFrom.getMessageType(), chatItem.forwardedFrom);

                String _text = AppUtils.conversionMessageType(chatItem.forwardedFrom.getMessageType());
                if (_text != null && _text.length() > 0) {
                    replayTo.setText(_text);
                } else {
                    replayTo.setText(chatItem.forwardedFrom.getMessage());
                }
            } else {
                RealmRoomMessage message = getRealmChat().where(RealmRoomMessage.class).equalTo(RealmRoomMessageFields.MESSAGE_ID, parseLong(chatItem.messageID)).findFirst();
                AppUtils.rightFileThumbnailIcon(thumbnail, chatItem.messageType, message);

                String _text = AppUtils.conversionMessageType(chatItem.messageType);
                if (_text != null && _text.length() > 0) {
                    replayTo.setText(_text);
                } else {
                    replayTo.setText(chatItem.messageText);
                }
            }
            if (chatType == CHANNEL) {
                RealmRoom realmRoom = getRealmChat().where(RealmRoom.class).equalTo(RealmRoomFields.ID, chatItem.roomId).findFirst();
                if (realmRoom != null) {
                    replayFrom.setText(realmRoom.getTitle());
                }
            } else {
                RealmRegisteredInfo userInfo = getRealmChat().where(RealmRegisteredInfo.class).equalTo(RealmRegisteredInfoFields.ID, parseLong(chatItem.senderID)).findFirst();
                if (userInfo != null) {
                    replayFrom.setText(userInfo.getDisplayName());
                }
            }

            //realm.close();
            // I set tag to retrieve it later when sending message
            mReplayLayout.setTag(chatItem);
        }
    }

    private void initLayoutChannelFooter() {
        LinearLayout layoutAttach = (LinearLayout) rootView.findViewById(R.id.chl_ll_attach);
        RelativeLayout layoutChannelFooter = (RelativeLayout) rootView.findViewById(R.id.chl_ll_channel_footer);

        layoutAttach.setVisibility(View.GONE);
        layoutChannelFooter.setVisibility(View.VISIBLE);

        txtChannelMute = (TextView) rootView.findViewById(R.id.chl_txt_mute_channel);
        txtChannelMute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onSelectRoomMenu("txtMuteNotification", (int) mRoomId);
            }
        });

        if (isMuteNotification) {
            txtChannelMute.setText(R.string.unmute);
        } else {
            txtChannelMute.setText(R.string.mute);
        }
    }

    private void initAppbarSelected() {
        ll_AppBarSelected = (LinearLayout) rootView.findViewById(R.id.chl_ll_appbar_selelected);

        RippleView rippleCloseAppBarSelected = (RippleView) rootView.findViewById(R.id.chl_ripple_close_layout);
        rippleCloseAppBarSelected.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                mAdapter.deselect();
                toolbar.setVisibility(View.VISIBLE);
                ll_AppBarSelected.setVisibility(View.GONE);
                clearReplyView();
            }
        });

        //  btnReplaySelected = (MaterialDesignTextView)  rootView.findViewById(R.id.chl_btn_replay_selected);
        rippleReplaySelected = (RippleView) rootView.findViewById(R.id.chl_ripple_replay_selected);

        if (chatType == CHANNEL) {
            if (channelRole == ChannelChatRole.MEMBER) {
                rippleReplaySelected.setVisibility(View.INVISIBLE);
            }
        } else {
            rippleReplaySelected.setVisibility(View.VISIBLE);
        }
        rippleReplaySelected.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                if (mAdapter != null && !mAdapter.getSelectedItems().isEmpty() && mAdapter.getSelectedItems().size() == 1) {
                    replay(mAdapter.getSelectedItems().iterator().next().mMessage);
                }
            }
        });
        RippleView rippleCopySelected = (RippleView) rootView.findViewById(R.id.chl_ripple_copy_selected);
        rippleCopySelected.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {

                copySelectedItemTextToClipboard();
            }
        });
        RippleView rippleForwardSelected = (RippleView) rootView.findViewById(R.id.chl_ripple_forward_selected);
        rippleForwardSelected.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                // forward selected messages to room list for selecting room
                if (mAdapter != null && mAdapter.getSelectedItems().size() > 0) {

                    mForwardMessages = getMessageStructFromSelectedItems();

                    //finish();

                    finishChat();
                }
            }
        });
        rippleDeleteSelected = (RippleView) rootView.findViewById(R.id.chl_ripple_delete_selected);
        rippleDeleteSelected.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {

                final ArrayList<Long> list = new ArrayList<Long>();

                G.handler.post(new Runnable() {
                    @Override
                    public void run() {

                        for (final AbstractMessage messageID : mAdapter.getSelectedItems()) {
                            try {
                                if (messageID != null && messageID.mMessage != null && messageID.mMessage.messageID != null) {
                                    Long messageId = parseLong(messageID.mMessage.messageID);
                                    list.add(messageId);
                                }
                            } catch (NullPointerException e) {
                                e.printStackTrace();
                            }
                        }

                        deleteSelectedMessageFromAdapter(list);

                        RealmRoomMessage.deleteSelectedMessages(getRealmChat(), mRoomId, list, chatType);
                    }
                });
            }
        });
        txtNumberOfSelected = (TextView) G.fragmentActivity.findViewById(R.id.chl_txt_number_of_selected);

        if (chatType == CHANNEL && channelRole == ChannelChatRole.MEMBER && !isNotJoin) {
            initLayoutChannelFooter();
        }
    }

    private void deleteSelectedMessageFromAdapter(ArrayList<Long> list) {
        for (Long mId : list) {
            try {
                mAdapter.removeMessage(mId);
                // remove tag from edtChat if the message has deleted
                if (edtChat.getTag() != null && edtChat.getTag() instanceof StructMessageInfo) {
                    if (mId == Long.parseLong(((StructMessageInfo) edtChat.getTag()).messageID)) {
                        edtChat.setTag(null);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (mAdapter != null) {
            int size = mAdapter.getItemCount();
            for (int i = 0; i < size; i++) {

                if (mAdapter.getItem(i) instanceof TimeItem) {
                    if (i < size - 1) {
                        if (mAdapter.getItem(i + 1) instanceof TimeItem) {
                            mAdapter.remove(i);
                        }
                    } else {
                        mAdapter.remove(i);
                    }
                }
            }
        }
    }

    private void initLayoutSearchNavigation() {
        //  ll_navigate_Message = (LinearLayout)  rootView.findViewById(R.id.ac_ll_message_navigation);
        //  btnUpMessage = (TextView)  rootView.findViewById(R.id.ac_btn_message_up);
        txtClearMessageSearch = (MaterialDesignTextView) rootView.findViewById(R.id.ac_btn_clear_message_search);
        //  btnDownMessage = (TextView) findViewById(R.id.ac_btn_message_down);
        //  txtMessageCounter = (TextView) findViewById(R.id.ac_txt_message_counter);

        //btnUpMessage.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View view) {
        //
        //        if (selectedPosition > 0) {
        //            deSelectMessage(selectedPosition);
        //            selectedPosition--;
        //            selectMessage(selectedPosition);
        //            recyclerView.scrollToPosition(selectedPosition);
        //            txtMessageCounter.setText(selectedPosition + 1 + " " + getString(of) + " " + messageCounter);
        //        }
        //    }
        //});

        //btnDownMessage.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View view) {
        //        if (selectedPosition < messageCounter - 1) {
        //            deSelectMessage(selectedPosition);
        //            selectedPosition++;
        //            selectMessage(selectedPosition);
        //            recyclerView.scrollToPosition(selectedPosition);
        //            txtMessageCounter.setText(selectedPosition + 1 + " " + getString(of) + messageCounter);
        //        }
        //    }
        //});

        final RippleView rippleClose = (RippleView) rootView.findViewById(R.id.chl_btn_close_ripple_search_message);
        rippleClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // deSelectMessage(selectedPosition);
                edtSearchMessage.setText("");
                btnHashLayoutClose.performClick();
            }
        });

        ll_Search = (LinearLayout) rootView.findViewById(R.id.ac_ll_search_message);
        RippleView rippleBack = (RippleView) rootView.findViewById(R.id.chl_ripple_back);
        rippleBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  deSelectMessage(selectedPosition);
                edtSearchMessage.setText("");
                ll_Search.setVisibility(View.GONE);
                rootView.findViewById(R.id.toolbarContainer).setVisibility(View.VISIBLE);
                //  ll_navigate_Message.setVisibility(View.GONE);
                // viewAttachFile.setVisibility(View.VISIBLE);

                btnHashLayoutClose.performClick();

                InputMethodManager imm = (InputMethodManager) G.fragmentActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        });
        //btnCloseLayoutSearch = (Button)  rootView.findViewById(R.id.ac_btn_close_layout_search_message);
        edtSearchMessage = (EditText) rootView.findViewById(R.id.chl_edt_search_message);
        edtSearchMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(final CharSequence charSequence, int i, int i1, int i2) {

                if (charSequence.length() > 0) {
                    if (FragmentChat.hashListener != null) {
                        FragmentChat.hashListener.complete(true, charSequence.toString(), "");
                    }
                } else {
                    btnHashLayoutClose.performClick();
                }

                //mAdapter.filter(charSequence);
                //
                //new Handler().postDelayed(new Runnable() {
                //    @Override
                //    public void run() {
                //        messageCounter = mAdapter.getAdapterItemCount();
                //
                //        if (messageCounter > 0) {
                //            selectedPosition = messageCounter - 1;
                //            recyclerView.scrollToPosition(selectedPosition);
                //
                //            if (charSequence.length() > 0) {
                //                selectMessage(selectedPosition);
                //                txtMessageCounter.setText(messageCounter + " " + getString(of) + " " + messageCounter);
                //            } else {
                //                txtMessageCounter.setText("0 " + getString(of) + " 0");
                //            }
                //        } else {
                //            txtMessageCounter.setText("0 " + getString(of) + " " + messageCounter);
                //            selectedPosition = 0;
                //        }
                //    }
                //}, 600);
                //
                if (charSequence.length() > 0) {
                    txtClearMessageSearch.setTextColor(Color.WHITE);
                    ((View) rippleClose).setEnabled(true);
                } else {
                    txtClearMessageSearch.setTextColor(Color.parseColor("#dededd"));
                    ((View) rippleClose).setEnabled(false);
                    //  txtMessageCounter.setText("0 " + getString(of) + " 0");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public void itemAdapterBottomSheet() {
        listPathString.clear();
        fastItemAdapter.clear();
        itemGalleryList = getAllShownImagesPath(G.fragmentActivity);
        try {
            HelperPermision.getCameraPermission(G.fragmentActivity, new OnGetPermission() {
                @Override
                public void Allow() throws IOException {

                    for (int i = 0; i < itemGalleryList.size(); i++) {
                        if (i == 0) {
                            fastItemAdapter.add(new AdapterCamera("").withIdentifier(99 + i));
                            fastItemAdapter.add(new AdapterBottomSheet(itemGalleryList.get(i)).withIdentifier(100 + i));
                        } else {
                            fastItemAdapter.add(new AdapterBottomSheet(itemGalleryList.get(i)).withIdentifier(100 + i));
                        }
                        isPermissionCamera = true;
                    }
                    G.handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (isAdded()) {
                                bottomSheetDialog.show();
                            }
                        }
                    }, 100);
                }

                @Override
                public void deny() {

                    G.handler.post(new Runnable() {
                        @Override
                        public void run() {
                            for (int i = 0; i < itemGalleryList.size(); i++) {
                                fastItemAdapter.add(new AdapterBottomSheet(itemGalleryList.get(i)).withIdentifier(100 + i));
                            }
                        }
                    });

                    G.handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (isAdded()) {
                                bottomSheetDialog.show();
                                fastItemAdapter.notifyDataSetChanged();
                            }
                        }
                    }, 100);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * *************************** inner classes ***************************
     */

    /**
     * *** SearchHash ***
     */

    private class SearchHash {

        private String hashString = "";
        public String lastMessageId = "";
        private int currentHashPosition;

        private ArrayList<String> hashList = new ArrayList<>();

        void setHashString(String hashString) {
            this.hashString = hashString.toLowerCase();
        }

        public void setPosition(String messageId) {

            if (mAdapter == null) {
                return;
            }

            if (lastMessageId.length() > 0) {
                mAdapter.toggleSelection(lastMessageId, false, null);
            }

            currentHashPosition = 0;
            hashList.clear();

            for (int i = 0; i < mAdapter.getAdapterItemCount(); i++) {
                if (mAdapter.getItem(i).mMessage != null) {

                    if (messageId.length() > 0) {
                        if (mAdapter.getItem(i).mMessage.messageID.equals(messageId)) {
                            currentHashPosition = hashList.size();
                            lastMessageId = messageId;
                            mAdapter.getItem(i).mMessage.isSelected = true;
                            mAdapter.notifyItemChanged(i);
                        }
                    }

                    String mText = mAdapter.getItem(i).mMessage.forwardedFrom != null ? mAdapter.getItem(i).mMessage.forwardedFrom.getMessage() : mAdapter.getItem(i).mMessage.messageText;

                    if (mText.toLowerCase().contains(hashString)) {
                        hashList.add(mAdapter.getItem(i).mMessage.messageID);
                    }
                }
            }

            if (messageId.length() == 0) {
                txtHashCounter.setText(hashList.size() + " / " + hashList.size());

                if (hashList.size() > 0) {
                    currentHashPosition = hashList.size() - 1;
                    goToSelectedPosition(hashList.get(currentHashPosition));
                }
            } else {
                txtHashCounter.setText((currentHashPosition + 1) + " / " + hashList.size());
            }
        }

        void downHash() {
            if (currentHashPosition < hashList.size() - 1) {

                currentHashPosition++;

                goToSelectedPosition(hashList.get(currentHashPosition));

                txtHashCounter.setText((currentHashPosition + 1) + " / " + hashList.size());
            }
        }

        void upHash() {
            if (currentHashPosition > 0) {

                currentHashPosition--;

                goToSelectedPosition(hashList.get(currentHashPosition));
                txtHashCounter.setText((currentHashPosition + 1) + " / " + hashList.size());
            }
        }

        private void goToSelectedPosition(String messageid) {

            mAdapter.toggleSelection(lastMessageId, false, null);

            lastMessageId = messageid;

            mAdapter.toggleSelection(lastMessageId, true, recyclerView);
        }
    }

    /**
     * *** VideoCompressor ***
     */

    class VideoCompressor extends AsyncTask<String, Void, StructCompress> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected StructCompress doInBackground(String... params) {
            if (params[0] == null) { // if data is null
                StructCompress structCompress = new StructCompress();
                structCompress.compress = false;
                return structCompress;
            }
            File file = new File(params[0]);
            long originalSize = file.length();

            StructCompress structCompress = new StructCompress();
            structCompress.path = params[1];
            structCompress.originalPath = params[0];
            try {
                structCompress.compress = MediaController.getInstance().convertVideo(params[0], params[1]);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
            structCompress.originalSize = originalSize;
            return structCompress;
        }

        @Override
        protected void onPostExecute(StructCompress structCompress) {
            super.onPostExecute(structCompress);
            if (structCompress.compress) {
                compressedPath.put(structCompress.path, true);
                for (StructUploadVideo structUploadVideo : structUploadVideos) {
                    if (structUploadVideo != null && structUploadVideo.filePath.equals(structCompress.path)) {
                        /**
                         * update new info after compress file with notify item
                         */

                        long fileSize = new File(structUploadVideo.filePath).length();
                        long duration = AndroidUtils.getAudioDuration(G.fragmentActivity, structUploadVideo.filePath) / 1000;

                        if (fileSize >= structCompress.originalSize) {
                            structUploadVideo.filePath = structCompress.originalPath;
                            mAdapter.updateVideoInfo(structUploadVideo.messageId, duration, structCompress.originalSize);
                        } else {
                            mAdapter.updateVideoInfo(structUploadVideo.messageId, duration, fileSize);
                        }

                        HelperUploadFile.startUploadTaskChat(structUploadVideo.roomId, chatType, structUploadVideo.filePath, structUploadVideo.messageId, structUploadVideo.messageType, structUploadVideo.message, structUploadVideo.replyMessageId, new HelperUploadFile.UpdateListener() {
                            @Override
                            public void OnProgress(int progress, FileUploadStructure struct) {
                                if (canUpdateAfterDownload) {
                                    insertItemAndUpdateAfterStartUpload(progress, struct);
                                }
                            }

                            @Override
                            public void OnError() {

                            }
                        });
                    }
                }
            }
        }
    }

    /**
     * *************************** Messaging ***************************
     */

    private void sendMessage(int requestCode, String filePath) {

        if (filePath == null || (filePath.length() == 0 && requestCode != AttachFile.request_code_contact_phone)) {
            clearReplyView();
            return;
        }

        Realm realm = Realm.getDefaultInstance();
        long messageId = SUID.id().get();
        final long updateTime = TimeUtils.currentLocalTime();
        ProtoGlobal.RoomMessageType messageType = null;
        String fileName = null;
        long duration = 0;
        long fileSize = 0;
        int[] imageDimens = {0, 0};
        final long senderID = G.userId;

        /**
         * check if path is uri detect real path from uri
         */
        String path = getFilePathFromUri(Uri.parse(filePath));
        if (path != null) {
            filePath = path;
        }

        StructMessageInfo messageInfo = null;

        switch (requestCode) {
            case IntentRequests.REQ_CROP:

                if (!filePath.toLowerCase().endsWith(".gif")) {
                    if (isMessageWrote()) {
                        messageType = IMAGE_TEXT;
                    } else {
                        messageType = ProtoGlobal.RoomMessageType.IMAGE;
                    }
                } else {
                    if (isMessageWrote()) {
                        messageType = GIF_TEXT;
                    } else {
                        messageType = ProtoGlobal.RoomMessageType.GIF;
                    }
                }

                fileName = new File(filePath).getName();
                fileSize = new File(filePath).length();
                imageDimens = AndroidUtils.getImageDimens(filePath);
                if (userTriesReplay()) {
                    messageInfo = new StructMessageInfo(getRealmChat(), mRoomId, Long.toString(messageId), Long.toString(senderID), ProtoGlobal.RoomMessageStatus.SENDING.toString(), messageType, MyType.SendType.send, null, filePath, updateTime, parseLong(((StructMessageInfo) mReplayLayout.getTag()).messageID));
                } else {
                    messageInfo = new StructMessageInfo(getRealmChat(), mRoomId, Long.toString(messageId), getWrittenMessage(), Long.toString(senderID), ProtoGlobal.RoomMessageStatus.SENDING.toString(), messageType, MyType.SendType.send, null, filePath, updateTime);
                }
                break;
            case AttachFile.request_code_TAKE_PICTURE:

                fileName = new File(filePath).getName();
                fileSize = new File(filePath).length();
                if (AndroidUtils.getImageDimens(filePath)[0] == 0 && AndroidUtils.getImageDimens(filePath)[1] == 0) {
                    G.handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, "Picture Not Loaded", Toast.LENGTH_SHORT).show();
                        }
                    });
                    return;
                }
                imageDimens = AndroidUtils.getImageDimens(filePath);
                if (isMessageWrote()) {
                    messageType = IMAGE_TEXT;
                } else {
                    messageType = ProtoGlobal.RoomMessageType.IMAGE;
                }
                if (userTriesReplay()) {
                    messageInfo = new StructMessageInfo(getRealmChat(), mRoomId, Long.toString(messageId), Long.toString(senderID), ProtoGlobal.RoomMessageStatus.SENDING.toString(), messageType, MyType.SendType.send, null, filePath, updateTime, parseLong(((StructMessageInfo) mReplayLayout.getTag()).messageID));
                } else {
                    messageInfo = new StructMessageInfo(getRealmChat(), mRoomId, Long.toString(messageId), getWrittenMessage(), Long.toString(senderID), ProtoGlobal.RoomMessageStatus.SENDING.toString(), messageType, MyType.SendType.send, null, filePath, updateTime);
                }

                break;

            case AttachFile.requestOpenGalleryForImageMultipleSelect:
                if (!filePath.toLowerCase().endsWith(".gif")) {
                    if (isMessageWrote()) {
                        messageType = IMAGE_TEXT;
                    } else {
                        messageType = ProtoGlobal.RoomMessageType.IMAGE;
                    }
                } else {
                    if (isMessageWrote()) {
                        messageType = GIF_TEXT;
                    } else {
                        messageType = ProtoGlobal.RoomMessageType.GIF;
                    }
                }

                fileName = new File(filePath).getName();
                fileSize = new File(filePath).length();
                imageDimens = AndroidUtils.getImageDimens(filePath);

                if (userTriesReplay()) {
                    messageInfo = new StructMessageInfo(getRealmChat(), mRoomId, Long.toString(messageId), Long.toString(senderID), ProtoGlobal.RoomMessageStatus.SENDING.toString(), messageType, MyType.SendType.send, null, filePath, updateTime, parseLong(((StructMessageInfo) mReplayLayout.getTag()).messageID));
                } else {
                    messageInfo = new StructMessageInfo(getRealmChat(), mRoomId, Long.toString(messageId), getWrittenMessage(), Long.toString(senderID), ProtoGlobal.RoomMessageStatus.SENDING.toString(), messageType, MyType.SendType.send, null, filePath, updateTime);
                }
                break;

            case AttachFile.requestOpenGalleryForVideoMultipleSelect:
            case request_code_VIDEO_CAPTURED:
                fileName = new File(filePath).getName();
                /**
                 * if video not compressed use from mainPath
                 */
                boolean compress = false;
                if (compressedPath.get(filePath) != null) {
                    compress = compressedPath.get(filePath);
                }
                if (compress) {
                    fileSize = new File(filePath).length();
                    duration = AndroidUtils.getAudioDuration(G.fragmentActivity, filePath) / 1000;
                } else {
                    fileSize = new File(mainVideoPath).length();
                    duration = AndroidUtils.getAudioDuration(G.fragmentActivity, mainVideoPath) / 1000;
                }

                if (isMessageWrote()) {
                    messageType = VIDEO_TEXT;
                } else {
                    messageType = VIDEO;
                }
                File videoFile = new File(filePath);
                String videoFileMime = FileUtils.getMimeType(videoFile);
                if (userTriesReplay()) {
                    messageInfo = new StructMessageInfo(getRealmChat(), mRoomId, Long.toString(messageId), Long.toString(senderID), ProtoGlobal.RoomMessageStatus.SENDING.toString(), messageType, MyType.SendType.send, videoFileMime, filePath, null, filePath, null, updateTime, parseLong(((StructMessageInfo) mReplayLayout.getTag()).messageID));
                } else {
                    messageInfo = new StructMessageInfo(getRealmChat(), mRoomId, Long.toString(messageId), Long.toString(senderID), getWrittenMessage(), ProtoGlobal.RoomMessageStatus.SENDING.toString(), messageType, MyType.SendType.send, videoFileMime, filePath, null, filePath, null, updateTime);
                }
                break;
            case AttachFile.request_code_pic_audi:
                fileName = new File(filePath).getName();
                fileSize = new File(filePath).length();
                duration = AndroidUtils.getAudioDuration(G.fragmentActivity, filePath) / 1000;
                if (isMessageWrote()) {
                    messageType = ProtoGlobal.RoomMessageType.AUDIO_TEXT;
                } else {
                    messageType = ProtoGlobal.RoomMessageType.AUDIO;
                }
                String songArtist = AndroidUtils.getAudioArtistName(filePath);
                long songDuration = AndroidUtils.getAudioDuration(G.fragmentActivity, filePath);

                messageInfo = StructMessageInfo.buildForAudio(getRealmChat(), mRoomId, messageId, senderID, ProtoGlobal.RoomMessageStatus.SENDING, messageType, MyType.SendType.send, updateTime, getWrittenMessage(), null, filePath, songArtist, songDuration, userTriesReplay() ? parseLong(((StructMessageInfo) mReplayLayout.getTag()).messageID) : -1);
                break;
            case AttachFile.request_code_pic_file:
            case AttachFile.request_code_open_document:

                fileName = new File(filePath).getName();
                fileSize = new File(filePath).length();
                if (isMessageWrote()) {
                    messageType = ProtoGlobal.RoomMessageType.FILE_TEXT;
                } else {
                    messageType = ProtoGlobal.RoomMessageType.FILE;
                }
                File fileFile = new File(filePath);
                String fileFileMime = FileUtils.getMimeType(fileFile);
                if (userTriesReplay()) {
                    messageInfo = new StructMessageInfo(getRealmChat(), mRoomId, Long.toString(messageId), Long.toString(senderID), ProtoGlobal.RoomMessageStatus.SENDING.toString(), messageType, MyType.SendType.send, fileFileMime, filePath, null, filePath, null, updateTime, parseLong(((StructMessageInfo) mReplayLayout.getTag()).messageID));
                } else {
                    messageInfo = new StructMessageInfo(getRealmChat(), mRoomId, Long.toString(messageId), Long.toString(senderID), getWrittenMessage(), ProtoGlobal.RoomMessageStatus.SENDING.toString(), messageType, MyType.SendType.send, fileFileMime, filePath, null, filePath, null, updateTime);
                }
                break;
            case AttachFile.request_code_contact_phone:
                if (latestUri == null) {
                    break;
                }
                ContactUtils contactUtils = new ContactUtils(G.fragmentActivity, latestUri);
                String name = contactUtils.retrieveName();
                String number = contactUtils.retrieveNumber();
                messageType = CONTACT;
                messageInfo = StructMessageInfo.buildForContact(getRealmChat(), mRoomId, messageId, senderID, MyType.SendType.send, updateTime, ProtoGlobal.RoomMessageStatus.SENDING, name, "", number, userTriesReplay() ? parseLong(((StructMessageInfo) mReplayLayout.getTag()).messageID) : -1);
                break;
            case AttachFile.request_code_paint:
                fileName = new File(filePath).getName();

                imageDimens = AndroidUtils.getImageDimens(filePath);
                if (isMessageWrote()) {
                    messageType = IMAGE_TEXT;
                } else {
                    messageType = ProtoGlobal.RoomMessageType.IMAGE;
                }
                if (userTriesReplay()) {
                    messageInfo = new StructMessageInfo(getRealmChat(), mRoomId, Long.toString(messageId), Long.toString(senderID), ProtoGlobal.RoomMessageStatus.SENDING.toString(), messageType, MyType.SendType.send, null, filePath, updateTime, parseLong(((StructMessageInfo) mReplayLayout.getTag()).messageID));
                } else {
                    messageInfo = new StructMessageInfo(getRealmChat(), mRoomId, Long.toString(messageId), getWrittenMessage(), Long.toString(senderID), ProtoGlobal.RoomMessageStatus.SENDING.toString(), messageType, MyType.SendType.send, null, filePath, updateTime);
                }
                break;
        }

        final ProtoGlobal.RoomMessageType finalMessageType = messageType;
        final String finalFilePath = filePath;
        final String finalFileName = fileName;
        final long finalDuration = duration;
        final long finalFileSize = fileSize;
        final int[] finalImageDimens = imageDimens;

        final StructMessageInfo finalMessageInfo = messageInfo;
        final long finalMessageId = messageId;
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                RealmRoomMessage roomMessage = realm.createObject(RealmRoomMessage.class, finalMessageId);

                roomMessage.setMessageType(finalMessageType);
                roomMessage.setMessage(getWrittenMessage());

                RealmRoomMessage.addTimeIfNeed(roomMessage, realm);
                RealmRoomMessage.isEmojiInText(roomMessage, getWrittenMessage());

                roomMessage.setStatus(ProtoGlobal.RoomMessageStatus.SENDING.toString());
                roomMessage.setRoomId(mRoomId);
                roomMessage.setAttachment(finalMessageId, finalFilePath, finalImageDimens[0], finalImageDimens[1], finalFileSize, finalFileName, finalDuration, LocalFileType.FILE);
                roomMessage.setUserId(senderID);
                roomMessage.setAuthorHash(G.authorHash);
                roomMessage.setShowMessage(true);
                roomMessage.setCreateTime(updateTime);
                if (userTriesReplay()) {
                    if (finalMessageInfo != null && finalMessageInfo.replayTo != null) {
                        roomMessage.setReplyTo(finalMessageInfo.replayTo);
                    }
                }

                /**
                 * make channel extra if room is channel
                 */
                if (chatType == CHANNEL) {
                    StructChannelExtra structChannelExtra = StructChannelExtra.makeDefaultStructure(finalMessageId, mRoomId);
                    finalMessageInfo.channelExtra = structChannelExtra;
                    RealmChannelExtra.convert(realm, structChannelExtra);
                    //roomMessage.setChannelExtra(RealmChannelExtra.convert(realm, structChannelExtra));
                }

                if (finalMessageType == CONTACT) {
                    RealmRoomMessageContact realmRoomMessageContact = realm.createObject(RealmRoomMessageContact.class, SUID.id().get());
                    realmRoomMessageContact.setFirstName(finalMessageInfo.userInfo.firstName);
                    realmRoomMessageContact.setLastName(finalMessageInfo.userInfo.lastName);
                    realmRoomMessageContact.addPhone(finalMessageInfo.userInfo.phone);
                    roomMessage.setRoomMessageContact(realmRoomMessageContact);
                }

                if (finalMessageType != CONTACT) {
                    finalMessageInfo.attachment = StructMessageAttachment.convert(roomMessage.getAttachment());
                }

                String makeThumbnailFilePath = "";
                if (finalMessageType == VIDEO || finalMessageType == VIDEO_TEXT) {
                    //if (compressedPath.get(finalFilePath)) {//(sharedPreferences.getInt(SHP_SETTING.KEY_TRIM, 1) == 0) ||
                    boolean compress = false;
                    if (compressedPath.get(finalFilePath) != null) {
                        compress = compressedPath.get(finalFilePath);
                    }
                    if (compress) {
                        makeThumbnailFilePath = finalFilePath;
                    } else {
                        makeThumbnailFilePath = mainVideoPath;
                    }
                }

                if (finalMessageType == VIDEO || finalMessageType == VIDEO_TEXT) {
                    Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(makeThumbnailFilePath, MediaStore.Video.Thumbnails.MINI_KIND);
                    if (bitmap != null) {
                        String path = AndroidUtils.saveBitmap(bitmap);
                        roomMessage.getAttachment().setLocalThumbnailPath(path);
                        roomMessage.getAttachment().setWidth(bitmap.getWidth());
                        roomMessage.getAttachment().setHeight(bitmap.getHeight());

                        finalMessageInfo.attachment.setLocalFilePath(roomMessage.getMessageId(), finalFilePath);
                        finalMessageInfo.attachment.width = bitmap.getWidth();
                        finalMessageInfo.attachment.height = bitmap.getHeight();
                    }

                    //if (compressedPath.get(finalFilePath)) {//(sharedPreferences.getInt(SHP_SETTING.KEY_TRIM, 1) == 0) ||
                    boolean compress = false;
                    if (compressedPath.get(finalFilePath) != null) {
                        compress = compressedPath.get(finalFilePath);
                    }
                    if (compress) {
                        HelperUploadFile.startUploadTaskChat(mRoomId, chatType, finalFilePath, finalMessageId, finalMessageType, getWrittenMessage(), StructMessageInfo.getReplyMessageId(finalMessageInfo), new HelperUploadFile.UpdateListener() {
                            @Override
                            public void OnProgress(int progress, FileUploadStructure struct) {
                                {
                                    insertItemAndUpdateAfterStartUpload(progress, struct);
                                }
                            }

                            @Override
                            public void OnError() {

                            }
                        });
                    } else {
                        compressingFiles.put(finalMessageId, null);
                        StructUploadVideo uploadVideo = new StructUploadVideo();
                        uploadVideo.filePath = finalFilePath;
                        uploadVideo.roomId = mRoomId;
                        uploadVideo.messageId = finalMessageId;
                        uploadVideo.messageType = finalMessageType;
                        uploadVideo.message = getWrittenMessage();
                        if (userTriesReplay()) {
                            uploadVideo.replyMessageId = parseLong(((StructMessageInfo) mReplayLayout.getTag()).messageID);
                        } else {
                            uploadVideo.replyMessageId = 0;
                        }
                        structUploadVideos.add(uploadVideo);

                        finalMessageInfo.attachment.compressing = G.context.getResources().getString(R.string.compressing);
                        G.handler.post(new Runnable() {
                            @Override
                            public void run() {
                                switchAddItem(new ArrayList<>(Collections.singletonList(finalMessageInfo)), false);
                            }
                        });
                    }
                }

                RealmRoom realmRoom = realm.where(RealmRoom.class).equalTo(RealmRoomFields.ID, roomMessage.getRoomId()).findFirst();
                if (realmRoom != null) {
                    realmRoom.setLastMessage(roomMessage);
                    //realmRoom.setUpdatedTime(roomMessage.getUpdateOrCreateTime());
                }
            }
        });

        if (finalMessageType != VIDEO && finalMessageType != VIDEO_TEXT) {
            if (finalMessageType != CONTACT) {

                HelperUploadFile.startUploadTaskChat(mRoomId, chatType, finalFilePath, finalMessageId, finalMessageType, getWrittenMessage(), StructMessageInfo.getReplyMessageId(finalMessageInfo), new HelperUploadFile.UpdateListener() {
                    @Override
                    public void OnProgress(int progress, FileUploadStructure struct) {
                        if (canUpdateAfterDownload) {
                            insertItemAndUpdateAfterStartUpload(progress, struct);
                        }
                    }

                    @Override
                    public void OnError() {

                    }
                });
            } else {
                ChatSendMessageUtil messageUtil = new ChatSendMessageUtil().newBuilder(chatType, finalMessageType, mRoomId).message(getWrittenMessage());
                messageUtil.contact(finalMessageInfo.userInfo.firstName, finalMessageInfo.userInfo.lastName, finalMessageInfo.userInfo.phone);
                if (userTriesReplay()) {
                    messageUtil.replyMessage(parseLong(((StructMessageInfo) mReplayLayout.getTag()).messageID));
                }
                messageUtil.sendMessage(Long.toString(finalMessageId));
            }

            if (finalMessageType == CONTACT) {
                messageInfo.channelExtra = new StructChannelExtra();
                mAdapter.add(new ContactItem(getRealmChat(), chatType, this).setMessage(messageInfo));
            }
        }

        if (userTriesReplay()) {
            mReplayLayout.setTag(null);
            G.handler.post(new Runnable() {
                @Override
                public void run() {
                    mReplayLayout.setVisibility(View.GONE);
                }
            });
        }

        realm.close();
        scrollToEnd();
    }

    public void sendCancelAction() {

        HelperSetAction.sendCancel(messageId);
    }

    public void sendPosition(final Double latitude, final Double longitude, final String imagePath) {
        sendCancelAction();

        //Realm realm = Realm.getDefaultInstance();
        final long id = SUID.id().get();

        getRealmChat().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                RealmRoomMessageLocation messageLocation = realm.createObject(RealmRoomMessageLocation.class, SUID.id().get());
                messageLocation.setLocationLat(latitude);
                messageLocation.setLocationLong(longitude);
                messageLocation.setImagePath(imagePath);
                RealmRoomMessage roomMessage = realm.createObject(RealmRoomMessage.class, id);
                roomMessage.setLocation(messageLocation);
                roomMessage.setCreateTime(TimeUtils.currentLocalTime());
                roomMessage.setMessageType(ProtoGlobal.RoomMessageType.LOCATION);
                roomMessage.setRoomId(mRoomId);
                roomMessage.setUserId(userId);
                roomMessage.setAuthorHash(G.authorHash);
                roomMessage.setStatus(ProtoGlobal.RoomMessageStatus.SENDING.toString());

                if (userTriesReplay()) {
                    RealmRoomMessage realmRoomMessage = realm.where(RealmRoomMessage.class).equalTo(RealmRoomMessageFields.MESSAGE_ID, parseLong(((StructMessageInfo) mReplayLayout.getTag()).messageID)).findFirst();
                    if (realmRoomMessage != null) {
                        roomMessage.setReplyTo(realmRoomMessage);
                    }
                }

                RealmRoom realmRoom = realm.where(RealmRoom.class).equalTo(RealmRoomFields.ID, mRoomId).findFirst();

                if (realmRoom != null) {
                    realmRoom.setLastMessage(roomMessage);
                }
            }
        });
        //realm.close();

        G.handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //+Realm realm1 = Realm.getDefaultInstance();
                RealmRoomMessage roomMessage = getRealmChat().where(RealmRoomMessage.class).equalTo(RealmRoomMessageFields.MESSAGE_ID, id).findFirst();
                switchAddItem(new ArrayList<>(Collections.singletonList(StructMessageInfo.convert(getRealmChat(), roomMessage))), false);
                chatSendMessageUtil.build(chatType, mRoomId, roomMessage);
                scrollToEnd();
                //realm1.close();
            }
        }, 300);

        clearReplyView();
    }

    /**
     * do forward actions if any message forward to this room
     */
    private void manageForwardedMessage() {
        if (mForwardMessages != null && !isChatReadOnly) {

            final LinearLayout ll_Forward = (LinearLayout) rootView.findViewById(R.id.ac_ll_forward);

            if (hasForward) {

                final ArrayList<Parcelable> mg = mForwardMessages;

                for (int i = 0; i < mg.size(); i++) {
                    /**
                     * send forwarded message with one second delay for each message
                     */
                    final int j = i;
                    G.handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            sendForwardedMessage((StructMessageInfo) Parcels.unwrap(mg.get(j)));
                        }
                    }, 1000 * j);
                }

                imvCancelForward.performClick();
            } else {
                imvCancelForward = (TextView) rootView.findViewById(R.id.cslhf_imv_cansel);
                imvCancelForward.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        ll_Forward.setVisibility(View.GONE);
                        hasForward = false;
                        mForwardMessages = null;

                        if (edtChat.getText().length() == 0) {

                            layoutAttachBottom.animate().alpha(1F).setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    layoutAttachBottom.setVisibility(View.VISIBLE);
                                }
                            }).start();
                            imvSendButton.animate().alpha(0F).setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    G.handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            imvSendButton.clearAnimation();
                                            imvSendButton.setVisibility(View.GONE);
                                        }
                                    });
                                }
                            }).start();
                        }
                    }
                });

                layoutAttachBottom.animate().alpha(0F).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        layoutAttachBottom.setVisibility(View.GONE);
                    }
                }).start();

                imvSendButton.animate().alpha(1F).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        G.handler.post(new Runnable() {
                            @Override
                            public void run() {
                                imvSendButton.clearAnimation();
                                imvSendButton.setVisibility(View.VISIBLE);
                            }
                        });
                    }
                }).start();

                int _count = mForwardMessages.size();
                String str = _count > 1 ? G.context.getResources().getString(R.string.messages_selected) : G.context.getResources().getString(R.string.message_selected);

                EmojiTextViewE emMessage = (EmojiTextViewE) rootView.findViewById(R.id.cslhf_txt_message);

                TextView txtForwardMessage = (TextView) rootView.findViewById(R.id.cslhf_txt_forward_from);
                txtForwardMessage.setTextColor(Color.parseColor(G.appBarColor));

                ImageView imvForwardIcon = (ImageView) rootView.findViewById(R.id.cslhs_imv_forward);
                imvForwardIcon.setColorFilter(Color.parseColor(G.appBarColor));

                if (HelperCalander.isLanguagePersian) {

                    emMessage.setText(HelperCalander.convertToUnicodeFarsiNumber(_count + " " + str));
                } else {

                    emMessage.setText(_count + " " + str);
                }

                hasForward = true;
                ll_Forward.setVisibility(View.VISIBLE);
            }
        }
    }

    private void sendForwardedMessage(final StructMessageInfo messageInfo) {

        final long messageId = SUID.id().get();

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                //final Realm realm = Realm.getDefaultInstance();

                getRealmChat().executeTransactionAsync(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        RealmRoomMessage roomMessage = realm.where(RealmRoomMessage.class).equalTo(RealmRoomMessageFields.MESSAGE_ID, parseLong(messageInfo.messageID)).findFirst();

                        if (roomMessage != null) {

                            RealmRoomMessage forwardedMessage = realm.createObject(RealmRoomMessage.class, messageId);
                            if (roomMessage.getForwardMessage() != null) {
                                forwardedMessage.setForwardMessage(roomMessage.getForwardMessage());
                                forwardedMessage.setHasMessageLink(roomMessage.getForwardMessage().getHasMessageLink());
                            } else {
                                forwardedMessage.setForwardMessage(roomMessage);
                                forwardedMessage.setHasMessageLink(roomMessage.getHasMessageLink());
                            }

                            forwardedMessage.setCreateTime(TimeUtils.currentLocalTime());
                            forwardedMessage.setMessageType(ProtoGlobal.RoomMessageType.TEXT);
                            forwardedMessage.setRoomId(mRoomId);
                            forwardedMessage.setStatus(ProtoGlobal.RoomMessageStatus.SENDING.toString());
                            forwardedMessage.setUserId(G.userId);
                            forwardedMessage.setShowMessage(true);
                        }
                    }
                }, new Realm.Transaction.OnSuccess() {
                    @Override
                    public void onSuccess() {

                        RealmRoomMessage forwardedMessage = getRealmChat().where(RealmRoomMessage.class).equalTo(RealmRoomMessageFields.MESSAGE_ID, messageId).findFirst();
                        if (forwardedMessage != null && forwardedMessage.isValid() && !forwardedMessage.isDeleted()) {
                            switchAddItem(new ArrayList<>(Collections.singletonList(StructMessageInfo.convert(getRealmChat(), forwardedMessage))), false);
                            scrollToEnd();
                            RealmRoomMessage roomMessage = getRealmChat().where(RealmRoomMessage.class).equalTo(RealmRoomMessageFields.MESSAGE_ID, parseLong(messageInfo.messageID)).findFirst();
                            chatSendMessageUtil.buildForward(chatType, forwardedMessage.getRoomId(), forwardedMessage, roomMessage.getRoomId(), roomMessage.getMessageId());
                        }

                        //realm.close();
                    }
                }, new Realm.Transaction.OnError() {
                    @Override
                    public void onError(Throwable error) {
                        //realm.close();
                    }
                });
            }
        });
    }

    private StructMessageInfo makeLayoutTime(long time) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);

        String timeString = TimeUtils.getChatSettingsTimeAgo(G.fragmentActivity, calendar.getTime());

        RealmRoomMessage timeMessage = new RealmRoomMessage();
        timeMessage.setMessageId(SUID.id().get());
        // -1 means time message
        timeMessage.setUserId(-1);
        timeMessage.setUpdateTime(time);
        timeMessage.setMessage(timeString);
        timeMessage.setMessageType(ProtoGlobal.RoomMessageType.TEXT);

        return StructMessageInfo.convert(getRealmChat(), timeMessage);
    }

    private void switchAddItem(ArrayList<StructMessageInfo> messageInfos, boolean addTop) {
        if (prgWaiting != null && messageInfos.size() > 0) {
            prgWaiting.setVisibility(View.GONE);
        }
        long identifier = SUID.id().get();
        for (StructMessageInfo messageInfo : messageInfos) {

            ProtoGlobal.RoomMessageType messageType;
            if (messageInfo.forwardedFrom != null) {
                if (messageInfo.forwardedFrom.isValid()) {
                    messageType = messageInfo.forwardedFrom.getMessageType();
                } else {
                    return;
                }
            } else {
                messageType = messageInfo.messageType;
            }

            if (!messageInfo.isTimeOrLogMessage() || (messageType == LOG)) {
                int index = 0;
                if (addTop) {
                    if (messageInfo.showTime) {
                        for (int i = 0; i < mAdapter.getAdapterItemCount(); i++) {
                            if (mAdapter.getAdapterItem(i) instanceof TimeItem) {
                                if (!RealmRoomMessage.isTimeDayDifferent(messageInfo.time, mAdapter.getAdapterItem(i).mMessage.time)) {
                                    mAdapter.remove(i);
                                }
                                break;
                            }
                        }
                        mAdapter.add(0, new TimeItem(getRealmChat(), this).setMessage(makeLayoutTime(messageInfo.time)).withIdentifier(identifier++));
                        index = 1;
                    }
                } else {

                    /**
                     * don't allow for add lower messageId to bottom of list
                     */
                    if (parseLong(messageInfo.messageID) > biggestMessageId) {
                        biggestMessageId = parseLong(messageInfo.messageID);
                    } else {
                        continue;
                    }

                    if (messageInfo.showTime) {
                        if (mAdapter.getItemCount() > 0) {
                            if (mAdapter.getAdapterItem(mAdapter.getItemCount() - 1).mMessage != null && RealmRoomMessage.isTimeDayDifferent(messageInfo.time, mAdapter.getAdapterItem(mAdapter.getItemCount() - 1).mMessage.time)) {
                                mAdapter.add(new TimeItem(getRealmChat(), this).setMessage(makeLayoutTime(messageInfo.time)).withIdentifier(identifier++));
                            }
                        } else {
                            mAdapter.add(new TimeItem(getRealmChat(), this).setMessage(makeLayoutTime(messageInfo.time)).withIdentifier(identifier++));
                        }
                    }
                }

                switch (messageType) {
                    case TEXT:
                        if (!addTop) {
                            mAdapter.add(new TextItem(getRealmChat(), chatType, this).setMessage(messageInfo).withIdentifier(identifier));
                        } else {
                            mAdapter.add(index, new TextItem(getRealmChat(), chatType, this).setMessage(messageInfo).withIdentifier(identifier));
                        }
                        break;
                    case IMAGE:
                        if (!addTop) {
                            mAdapter.add(new ImageItem(getRealmChat(), chatType, this).setMessage(messageInfo).withIdentifier(identifier));
                        } else {
                            mAdapter.add(index, new ImageItem(getRealmChat(), chatType, this).setMessage(messageInfo).withIdentifier(identifier));
                        }
                        break;
                    case IMAGE_TEXT:
                        if (!addTop) {
                            mAdapter.add(new ImageWithTextItem(getRealmChat(), chatType, this).setMessage(messageInfo).withIdentifier(identifier));
                        } else {
                            mAdapter.add(index, new ImageWithTextItem(getRealmChat(), chatType, this).setMessage(messageInfo).withIdentifier(identifier));
                        }
                        break;
                    case VIDEO:
                        if (!addTop) {
                            mAdapter.add(new VideoItem(getRealmChat(), chatType, this).setMessage(messageInfo).withIdentifier(identifier));
                        } else {
                            mAdapter.add(index, new VideoItem(getRealmChat(), chatType, this).setMessage(messageInfo).withIdentifier(identifier));
                        }
                        break;
                    case VIDEO_TEXT:
                        if (!addTop) {
                            mAdapter.add(new VideoWithTextItem(getRealmChat(), chatType, this).setMessage(messageInfo).withIdentifier(identifier));
                        } else {
                            mAdapter.add(index, new VideoWithTextItem(getRealmChat(), chatType, this).setMessage(messageInfo).withIdentifier(identifier));
                        }
                        break;
                    case LOCATION:
                        if (!addTop) {
                            mAdapter.add(new LocationItem(getRealmChat(), chatType, this).setMessage(messageInfo).withIdentifier(identifier));
                        } else {
                            mAdapter.add(index, new LocationItem(getRealmChat(), chatType, this).setMessage(messageInfo).withIdentifier(identifier));
                        }
                        break;
                    case FILE:
                    case FILE_TEXT:
                        if (!addTop) {
                            mAdapter.add(new FileItem(getRealmChat(), chatType, this).setMessage(messageInfo).withIdentifier(identifier));
                        } else {
                            mAdapter.add(index, new FileItem(getRealmChat(), chatType, this).setMessage(messageInfo).withIdentifier(identifier));
                        }
                        break;
                    case VOICE:
                        if (!addTop) {
                            mAdapter.add(new VoiceItem(getRealmChat(), chatType, this).setMessage(messageInfo).withIdentifier(identifier));
                        } else {
                            mAdapter.add(index, new VoiceItem(getRealmChat(), chatType, this).setMessage(messageInfo).withIdentifier(identifier));
                        }
                        break;
                    case AUDIO:
                    case AUDIO_TEXT:
                        if (!addTop) {
                            mAdapter.add(new AudioItem(getRealmChat(), chatType, this).setMessage(messageInfo).withIdentifier(identifier));
                        } else {
                            mAdapter.add(index, new AudioItem(getRealmChat(), chatType, this).setMessage(messageInfo).withIdentifier(identifier));
                        }
                        break;
                    case CONTACT:
                        if (!addTop) {
                            mAdapter.add(new ContactItem(getRealmChat(), chatType, this).setMessage(messageInfo).withIdentifier(identifier));
                        } else {
                            mAdapter.add(index, new ContactItem(getRealmChat(), chatType, this).setMessage(messageInfo).withIdentifier(identifier));
                        }
                        break;
                    case GIF:
                        if (!addTop) {
                            mAdapter.add(new GifItem(getRealmChat(), chatType, this).setMessage(messageInfo).withIdentifier(identifier));
                        } else {
                            mAdapter.add(index, new GifItem(getRealmChat(), chatType, this).setMessage(messageInfo).withIdentifier(identifier));
                        }
                        break;
                    case GIF_TEXT:
                        if (!addTop) {
                            mAdapter.add(new GifWithTextItem(getRealmChat(), chatType, this).setMessage(messageInfo).withIdentifier(identifier));
                        } else {
                            mAdapter.add(index, new GifWithTextItem(getRealmChat(), chatType, this).setMessage(messageInfo).withIdentifier(identifier));
                        }
                        break;
                    case LOG:
                        if (messageInfo.showMessage) {
                            if (!addTop) {
                                mAdapter.add(new LogItem(getRealmChat(), this).setMessage(messageInfo).withIdentifier(identifier));
                            } else {
                                mAdapter.add(index, new LogItem(getRealmChat(), this).setMessage(messageInfo).withIdentifier(identifier));
                            }
                        }
                        break;
                }
            }
            identifier++;
        }
    }

    /**
     * **********************************************************************
     * *************************** Message Loader ***************************
     * **********************************************************************
     */

    private boolean addToView; // allow to message for add to recycler view or no
    private boolean topMore = true; // more message exist in local for load in up direction (topMore default value is true for allowing that try load top message )
    private boolean bottomMore; // more message exist in local for load in bottom direction
    private boolean isWaitingForHistoryUp; // client send request for getHistory, avoid for send request again
    private boolean isWaitingForHistoryDown; // client send request for getHistory, avoid for send request again
    private boolean allowGetHistoryUp = true;
    // after insuring for get end of message from server set this false. (set false in history error maybe was wrong , because maybe this was for another error not end  of message, (hint: can check error code for end of message from history))
    private boolean allowGetHistoryDown = true;
    // after insuring for get end of message from server set this false. (set false in history error maybe was wrong , because maybe this was for another error not end  of message, (hint: can check error code for end of message from history))
    private boolean firstUp = true; // if is firstUp getClientRoomHistory with low limit in UP direction
    private boolean firstDown = true; // if is firstDown getClientRoomHistory with low limit in DOWN direction
    private long gapMessageIdUp; // messageId that maybe lost in local
    private long gapMessageIdDown; // messageId that maybe lost in local
    private long reachMessageIdUp; // messageId that will be checked after getHistory for detect reached to that or no
    private long reachMessageIdDown; // messageId that will be checked after getHistory for detect reached to that or no
    private long startFutureMessageIdUp;
    // for get history from local or online in next step use from this param, ( hint : don't use from adapter items, because maybe this item was deleted and in this state messageId for get history won't be detected.
    private long startFutureMessageIdDown;
    // for get history from local or online in next step use from this param, ( hint : don't use from adapter items, because maybe this item was deleted and in this state messageId for get history won't be detected.
    private long progressIdentifierUp; // store identifier for Up progress item and use it if progress not removed from view after check 'instanceOf' in 'progressItem' method
    private long progressIdentifierDown; // store identifier for Down progress item and use it if progress not removed from view after check 'instanceOf' in 'progressItem' method
    private int firstVisiblePosition; // difference between start of adapter item and items that Showing.
    private int visibleItemCount; // visible item in recycler view
    private int totalItemCount; // all item in recycler view
    private int scrollEnd = 80; // (hint: It should be less than MessageLoader.LOCAL_LIMIT ) to determine the limits to get to the bottom or top of the list

    private void getMessages() {
        //+Realm realm = Realm.getDefaultInstance();

        ProtoClientGetRoomHistory.ClientGetRoomHistory.Direction direction;
        ArrayList<StructMessageInfo> messageInfos = new ArrayList<>();
        /**
         * get message in first enter to chat if has unread get message with down direction
         */
        RealmResults<RealmRoomMessage> results;
        RealmResults<RealmRoomMessage> resultsDown = null;
        RealmResults<RealmRoomMessage> resultsUp;
        long fetchMessageId = 0; // with this value realm will be queried for get message
        if (hasUnread() || hasSavedState()) {

            if (firstUnreadMessage == null || !firstUnreadMessage.isManaged() || !firstUnreadMessage.isValid() || firstUnreadMessage.isDeleted()) {
                firstUnreadMessage = getFirstUnreadMessage(getRealmChat());
            }

            /**
             * show unread layout and also set firstUnreadMessageId in startFutureMessageIdUp
             * for try load top message and also topMore default value is true for this target
             */
            if (hasSavedState()) {
                fetchMessageId = getSavedState();

                if (hasUnread()) {
                    if (firstUnreadMessage == null) {
                        resetMessagingValue();
                        getMessages();
                        return;
                    }
                    countNewMessage = unreadCount;
                    txtNewUnreadMessage.setVisibility(View.VISIBLE);
                    txtNewUnreadMessage.setText(countNewMessage + "");
                    llScrollNavigate.setVisibility(View.VISIBLE);
                    firstUnreadMessageInChat = firstUnreadMessage;
                }
            } else {
                if (firstUnreadMessage == null) {
                    resetMessagingValue();
                    getMessages();
                    return;
                }
                unreadLayoutMessage();
                fetchMessageId = firstUnreadMessage.getMessageId();
            }
            startFutureMessageIdUp = fetchMessageId;

            // we have firstUnreadMessage but for gapDetection method we need RealmResult so get this message with query; if we change gap detection method will be can use from firstUnreadMessage
            resultsDown = getRealmChat().where(RealmRoomMessage.class).equalTo(RealmRoomMessageFields.ROOM_ID, mRoomId).notEqualTo(RealmRoomMessageFields.CREATE_TIME, 0).equalTo(RealmRoomMessageFields.DELETED, false).equalTo(RealmRoomMessageFields.SHOW_MESSAGE, true).equalTo(RealmRoomMessageFields.MESSAGE_ID, fetchMessageId).findAll();

            addToView = false;
            direction = DOWN;
        } else {
            addToView = true;
            direction = UP;
        }

        resultsUp = getRealmChat().where(RealmRoomMessage.class).equalTo(RealmRoomMessageFields.ROOM_ID, mRoomId).notEqualTo(RealmRoomMessageFields.CREATE_TIME, 0).equalTo(RealmRoomMessageFields.DELETED, false).equalTo(RealmRoomMessageFields.SHOW_MESSAGE, true).findAllSorted(RealmRoomMessageFields.MESSAGE_ID, Sort.DESCENDING);

        long gapMessageId;
        if (direction == DOWN) {
            resultsUp =
                    getRealmChat().where(RealmRoomMessage.class).equalTo(RealmRoomMessageFields.ROOM_ID, mRoomId).lessThanOrEqualTo(RealmRoomMessageFields.MESSAGE_ID, fetchMessageId).notEqualTo(RealmRoomMessageFields.CREATE_TIME, 0).equalTo(RealmRoomMessageFields.DELETED, false).equalTo(RealmRoomMessageFields.SHOW_MESSAGE, true).findAllSorted(RealmRoomMessageFields.CREATE_TIME, Sort.DESCENDING);
            /**
             * if for UP state client have message detect gap otherwise try for get online message
             * because maybe client have message but not exist in Realm yet
             */
            if (resultsUp.size() > 1) {
                gapDetection(resultsUp, UP);
            } else {
                getOnlineMessage(fetchMessageId, UP);
            }

            results = resultsDown;
            gapMessageId = gapDetection(results, direction);
        } else {
            results = resultsUp;
            gapMessageId = gapDetection(resultsUp, UP);
        }

        if (results.size() > 0) {

            Object[] object = getLocalMessage(getRealmChat(), mRoomId, results.first().getMessageId(), gapMessageId, true, direction);
            messageInfos = (ArrayList<StructMessageInfo>) object[0];
            if (messageInfos.size() > 0) {
                if (direction == UP) {
                    topMore = (boolean) object[1];
                    startFutureMessageIdUp = parseLong(messageInfos.get(messageInfos.size() - 1).messageID);
                } else {
                    bottomMore = (boolean) object[1];
                    startFutureMessageIdDown = parseLong(messageInfos.get(messageInfos.size() - 1).messageID);
                }
            } else {
                if (direction == UP) {
                    startFutureMessageIdUp = 0;
                } else {
                    startFutureMessageIdDown = 0;
                }
            }

            /**
             * if gap is exist ,check that reached to gap or not and if
             * reached send request to server for clientGetRoomHistory
             */
            if (gapMessageId > 0) {
                boolean hasSpaceToGap = (boolean) object[2];
                if (!hasSpaceToGap) {

                    long oldMessageId = 0;
                    if (messageInfos.size() > 0) {
                        /**
                         * this code is correct for UP or DOWN load message result
                         */
                        oldMessageId = parseLong(messageInfos.get(messageInfos.size() - 1).messageID);
                    }
                    /**
                     * send request to server for clientGetRoomHistory
                     */
                    getOnlineMessage(oldMessageId, direction);
                }
            } else {
                /**
                 * if gap not exist and also not exist more message in local
                 * send request for get message from server
                 */
                if ((direction == UP && !topMore) || (direction == DOWN && !bottomMore)) {
                    if (messageInfos.size() > 0) {
                        getOnlineMessage(parseLong(messageInfos.get(messageInfos.size() - 1).messageID), direction);
                    } else {
                        getOnlineMessage(0, direction);
                    }
                }
            }
        } else {
            /** send request to server for get message.
             * if direction is DOWN check again realmRoomMessage for detection
             * that exist any message without checking deleted state and if
             * exist use from that messageId instead of zero for getOnlineMessage
             */
            long oldMessageId = 0;
            if (direction == DOWN) {
                RealmRoomMessage realmRoomMessage = getRealmChat().where(RealmRoomMessage.class).equalTo(RealmRoomMessageFields.ROOM_ID, mRoomId).notEqualTo(RealmRoomMessageFields.CREATE_TIME, 0).equalTo(RealmRoomMessageFields.SHOW_MESSAGE, true).equalTo(RealmRoomMessageFields.MESSAGE_ID, fetchMessageId).findFirst();
                if (realmRoomMessage != null) {
                    oldMessageId = realmRoomMessage.getMessageId();
                }
            }

            getOnlineMessage(oldMessageId, direction);
        }

        if (direction == UP) {
            switchAddItem(messageInfos, true);
        } else {
            switchAddItem(messageInfos, false);
            if (hasSavedState()) {
                int position = mAdapter.findPositionByMessageId(savedScrollMessageId);
                LinearLayoutManager linearLayout = (LinearLayoutManager) recyclerView.getLayoutManager();
                linearLayout.scrollToPositionWithOffset(position, 0);
                savedScrollMessageId = 0;
            }
        }

        /**
         * make scrollListener for detect change in scroll and load more in chat
         */
        scrollListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                visibleItemCount = (recyclerView.getLayoutManager()).getChildCount();
                totalItemCount = (recyclerView.getLayoutManager()).getItemCount();
                firstVisiblePosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();

                if (firstVisiblePosition < scrollEnd) {
                    /**
                     * scroll to top
                     */
                    loadMessage(UP);
                } else if (firstVisiblePosition + visibleItemCount >= (totalItemCount - scrollEnd)) {
                    /**
                     * scroll to bottom
                     */
                    loadMessage(DOWN);
                }
            }
        };

        recyclerView.addOnScrollListener(scrollListener);
        //realm.close();
    }

    /**
     * manage load message from local or from server(online)
     */
    private void loadMessage(final ProtoClientGetRoomHistory.ClientGetRoomHistory.Direction direction) {
        long gapMessageId;
        long startFutureMessageId;
        if (direction == UP) {
            gapMessageId = gapMessageIdUp;
            startFutureMessageId = startFutureMessageIdUp;
        } else {
            gapMessageId = gapMessageIdDown;
            startFutureMessageId = startFutureMessageIdDown;
        }
        if ((direction == UP && topMore) || (direction == DOWN && bottomMore)) {
            Object[] object = getLocalMessage(getRealmChat(), mRoomId, startFutureMessageId, gapMessageId, false, direction);
            if (direction == UP) {
                topMore = (boolean) object[1];
            } else {
                bottomMore = (boolean) object[1];
            }
            final ArrayList<StructMessageInfo> structMessageInfos = (ArrayList<StructMessageInfo>) object[0];
            if (structMessageInfos.size() > 0) {
                if (direction == UP) {
                    startFutureMessageIdUp = parseLong(structMessageInfos.get(structMessageInfos.size() - 1).messageID);
                } else {
                    startFutureMessageIdDown = parseLong(structMessageInfos.get(structMessageInfos.size() - 1).messageID);
                }
            } else {
                /**
                 * don't set zero. when user come to room for first time with -@roomId-
                 * for example : @public ,this block will be called and set zero this value and finally
                 * don't allow to user for get top history, also that sounds this block isn't helpful
                 */
                //if (direction == UP) {
                //    startFutureMessageIdUp = 0;
                //} else {
                //    startFutureMessageIdDown = 0;
                //}
            }

            recyclerView.post(new Runnable() {
                @Override
                public void run() {
                    if (direction == UP) {
                        switchAddItem(structMessageInfos, true);
                    } else {
                        switchAddItem(structMessageInfos, false);
                    }
                }
            });

            /**
             * if gap is exist ,check that reached to gap or not and if
             * reached send request to server for clientGetRoomHistory
             */
            if (gapMessageId > 0) {
                boolean hasSpaceToGap = (boolean) object[2];
                if (!hasSpaceToGap) {
                    /**
                     * send request to server for clientGetRoomHistory
                     */
                    long oldMessageId;
                    if (structMessageInfos.size() > 0) {
                        oldMessageId = parseLong(structMessageInfos.get(structMessageInfos.size() - 1).messageID);
                    } else {
                        oldMessageId = gapMessageId;
                    }

                    getOnlineMessage(oldMessageId, direction);
                }
            }
        } else if (gapMessageId > 0) {
            /**
             * detect old messageId that should get history from server with that
             * (( hint : in scroll state never should get online message with messageId = 0
             * in some cases maybe startFutureMessageIdUp Equal to zero , so i used from this if.))
             */
            if (startFutureMessageId != 0) {
                getOnlineMessage(startFutureMessageId, direction);
            }
        } else {

            if (((direction == UP && allowGetHistoryUp) || (direction == DOWN && allowGetHistoryDown)) && startFutureMessageId != 0) {
                getOnlineMessage(startFutureMessageId, direction);
            }
        }
    }

    /**
     * get message history from server
     *
     * @param oldMessageId if set oldMessageId=0 messages will be get from latest message that exist in server
     */
    private void getOnlineMessage(final long oldMessageId, final ProtoClientGetRoomHistory.ClientGetRoomHistory.Direction direction) {
        if ((direction == UP && !isWaitingForHistoryUp && allowGetHistoryUp) || (direction == DOWN && !isWaitingForHistoryDown && allowGetHistoryDown)) {

            long reachMessageId;
            if (direction == UP) {
                reachMessageId = reachMessageIdUp;
                isWaitingForHistoryUp = true;
            } else {
                reachMessageId = reachMessageIdDown;
                isWaitingForHistoryDown = true;
            }

            /**
             * show progress when start for get history from server
             */
            progressItem(SHOW, direction);

            int limit = Config.LIMIT_GET_HISTORY_NORMAL;
            if ((firstUp && direction == UP) || (firstDown && direction == DOWN)) {
                limit = Config.LIMIT_GET_HISTORY_LOW;
            }

            MessageLoader.getOnlineMessage(getRealmChat(), mRoomId, oldMessageId, reachMessageId, limit, direction, new OnMessageReceive() {
                @Override
                public void onMessage(final long roomId, long startMessageId, long endMessageId, boolean gapReached, boolean jumpOverLocal, String directionString) {
                    if (roomId != mRoomId) {
                        return;
                    }
                    hideProgress();
                    /**
                     * hide progress received history
                     */
                    progressItem(HIDE, directionString);

                    //Realm realm = Realm.getDefaultInstance();
                    RealmResults<RealmRoomMessage> realmRoomMessages;
                    Sort sort;
                    ProtoClientGetRoomHistory.ClientGetRoomHistory.Direction directionEnum;
                    if (directionString.equals(UP.toString())) {
                        firstUp = false;
                        startFutureMessageIdUp = startMessageId;
                        directionEnum = UP;
                        sort = Sort.DESCENDING;
                        isWaitingForHistoryUp = false;
                    } else {
                        firstDown = false;
                        startFutureMessageIdDown = endMessageId;
                        directionEnum = DOWN;
                        sort = Sort.ASCENDING;
                        isWaitingForHistoryDown = false;
                    }
                    realmRoomMessages = getRealmChat().where(RealmRoomMessage.class).equalTo(RealmRoomMessageFields.ROOM_ID, roomId).notEqualTo(RealmRoomMessageFields.DELETED, true).between(RealmRoomMessageFields.MESSAGE_ID, startMessageId, endMessageId).findAllSorted(RealmRoomMessageFields.MESSAGE_ID, sort);
                    MessageLoader.sendMessageStatus(roomId, realmRoomMessages, chatType, ProtoGlobal.RoomMessageStatus.SEEN, getRealmChat());

                    if (realmRoomMessages.size() > 0) {
                        G.handler.post(new Runnable() {
                            @Override
                            public void run() {
                            }
                        });
                    }

                    /**
                     * I do this for set addToView true
                     */
                    if (directionEnum == DOWN && realmRoomMessages.size() < (Config.LIMIT_GET_HISTORY_NORMAL - 1)) {
                        getOnlineMessage(startFutureMessageIdDown, directionEnum);
                    }

                    /**
                     * when reached to gap and not jumped over local, set gapMessageIdUp = 0; do this action
                     * means that gap not exist (need this value for future get message) set topMore/bottomMore
                     * local after that gap reached true for allow that get message from
                     */
                    if (gapReached && !jumpOverLocal) {
                        if (directionEnum == UP) {
                            gapMessageIdUp = 0;
                            reachMessageIdUp = 0;
                            topMore = true;
                        } else {
                            gapMessageIdDown = 0;
                            reachMessageIdDown = 0;
                            bottomMore = true;
                        }

                        gapDetection(realmRoomMessages, directionEnum);
                    } else if ((directionEnum == UP && isReachedToTopView()) || directionEnum == DOWN && isReachedToBottomView()) {
                        /**
                         * check this state because if user is near to top view and not scroll get top message from server
                         */
                        //getOnlineMessage(startFutureMessageId, directionEnum);
                    }

                    final ArrayList<StructMessageInfo> structMessageInfos = new ArrayList<>();
                    for (RealmRoomMessage realmRoomMessage : realmRoomMessages) {
                        structMessageInfos.add(StructMessageInfo.convert(getRealmChat(), realmRoomMessage));
                    }

                    if (directionString.equals(UP.toString())) {
                        switchAddItem(structMessageInfos, true);
                    } else {
                        switchAddItem(structMessageInfos, false);
                    }

                    //realm.close();
                }

                @Override
                public void onError(int majorCode, int minorCode, long messageIdGetHistory, String direction) {
                    hideProgress();
                    /**
                     * hide progress if have any error
                     */
                    progressItem(HIDE, direction);

                    if (majorCode == 617) {

                        if (!isWaitingForHistoryUp && !isWaitingForHistoryDown && mAdapter.getItemCount() == 0) {
                            G.handler.post(new Runnable() {
                                @Override
                                public void run() {
                                }
                            });
                        }

                        if (direction.equals(UP.toString())) {
                            isWaitingForHistoryUp = false;
                            isWaitingForHistoryUp = false;
                            allowGetHistoryUp = false;
                            G.handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    //TODO [Saeed Mozaffari] [2017-03-06 9:50 AM] - for avoid from 'Inconsistency detected. Invalid item position' error i set notifyDataSetChanged. Find Solution And Clear it!!!
                                    mAdapter.notifyDataSetChanged();
                                }
                            });
                        } else {
                            addToView = true;
                            isWaitingForHistoryDown = false;
                            allowGetHistoryDown = false;
                        }
                    }

                    /**
                     * if time out came up try again for get history with previous value
                     */
                    if (majorCode == 5) {
                        if (direction.equals(UP.toString())) {
                            getOnlineMessage(messageIdGetHistory, UP);
                        } else {
                            getOnlineMessage(messageIdGetHistory, DOWN);
                        }
                    }
                }
            });
        }
    }

    /**
     * detect gap exist in this room or not
     * (hint : if gapMessageId==0 means that gap not exist)
     * if gapMessageIdUp exist, not compute again
     */
    private long gapDetection(RealmResults<RealmRoomMessage> results, ProtoClientGetRoomHistory.ClientGetRoomHistory.Direction direction) {
        if (((direction == UP && gapMessageIdUp == 0) || (direction == DOWN && gapMessageIdDown == 0)) && results.size() > 0) {
            Object[] objects = MessageLoader.gapExist(getRealmChat(), mRoomId, results.first().getMessageId(), direction);
            if (direction == UP) {
                reachMessageIdUp = (long) objects[1];
                return gapMessageIdUp = (long) objects[0];
            } else {
                reachMessageIdDown = (long) objects[1];
                return gapMessageIdDown = (long) objects[0];
            }
        }
        return 0;
    }

    /**
     * return true if now view is near to top
     */
    private boolean isReachedToTopView() {
        return firstVisiblePosition <= 5;
    }

    /**
     * return true if now view is near to bottom
     */
    private boolean isReachedToBottomView() {
        return (firstVisiblePosition + visibleItemCount >= (totalItemCount - 5));
    }

    /**
     * make unread layout message
     */
    private void unreadLayoutMessage() {
        int unreadMessageCount = unreadCount;
        if (unreadMessageCount > 0) {
            //int position = mAdapter.findPositionByMessageId(realmRoomMessage.getMessageId());
            //if (position != -1) {
            RealmRoomMessage unreadMessage = new RealmRoomMessage();
            unreadMessage.setMessageId(TimeUtils.currentLocalTime());
            // -1 means time message
            unreadMessage.setUserId(-1);
            unreadMessage.setMessage(unreadMessageCount + " " + G.context.getResources().getString(R.string.unread_message));
            unreadMessage.setMessageType(ProtoGlobal.RoomMessageType.TEXT);
            mAdapter.add(0, new UnreadMessage(getRealmChat(), FragmentChat.this).setMessage(StructMessageInfo.convert(getRealmChat(), unreadMessage)).withIdentifier(SUID.id().get()));
            //}
        }
    }

    /**
     * return first unread message for current room
     * (reason : use from this method for avoid from closed realm error)
     */
    private RealmRoomMessage getFirstUnreadMessage(Realm realm) {
        RealmRoom realmRoom = realm.where(RealmRoom.class).equalTo(RealmRoomFields.ID, mRoomId).findFirst();
        if (realmRoom != null) {
            return realmRoom.getFirstUnreadMessage();
        }
        return null;
    }

    /**
     * check that this room has unread or no
     */
    private boolean hasUnread() {
        return unreadCount > 0;
    }

    /**
     * check that this room has saved state or no
     */
    private boolean hasSavedState() {
        return savedScrollMessageId > 0;
    }

    /**
     * return saved scroll messageId
     */
    private long getSavedState() {
        return savedScrollMessageId;
    }

    /**
     * manage progress state in adapter
     *
     * @param progressState SHOW or HIDE state detect with enum
     * @param direction define direction for show progress in UP or DOWN
     */
    private void progressItem(final ProgressState progressState, final ProtoClientGetRoomHistory.ClientGetRoomHistory.Direction direction) {
        G.handler.post(new Runnable() {
            @Override
            public void run() {
                int progressIndex = 0;
                if (direction == DOWN) {
                    progressIndex = mAdapter.getAdapterItemCount() - 1;
                }
                if (progressState == SHOW) {
                    if ((mAdapter.getAdapterItemCount() > 0) && !(mAdapter.getAdapterItem(progressIndex) instanceof ProgressWaiting)) {
                        recyclerView.post(new Runnable() {
                            @Override
                            public void run() {
                                if (direction == DOWN) {
                                    progressIdentifierDown = SUID.id().get();
                                    mAdapter.add(new ProgressWaiting(getRealmChat(), FragmentChat.this).withIdentifier(progressIdentifierDown));
                                } else {
                                    progressIdentifierUp = SUID.id().get();
                                    mAdapter.add(0, new ProgressWaiting(getRealmChat(), FragmentChat.this).withIdentifier(progressIdentifierUp));
                                }
                            }
                        });
                    }
                } else {
                    /**
                     * i do this action with delay because sometimes instance wasn't successful
                     * for detect progress so client need delay for detect this instance
                     */
                    if ((mAdapter.getItemCount() > 0) && (mAdapter.getAdapterItem(progressIndex) instanceof ProgressWaiting)) {
                        mAdapter.remove(progressIndex);
                    } else {
                        G.handler.post(new Runnable() {
                            @Override
                            public void run() {
                                /**
                                 * if not detected progress item for remove use from item identifier and remove progress item
                                 */
                                if (direction == DOWN && progressIdentifierDown != 0) {
                                    for (int i = (mAdapter.getItemCount() - 1); i >= 0; i--) {
                                        if (mAdapter.getItem(i).getIdentifier() == progressIdentifierDown) {
                                            mAdapter.remove(i);
                                            progressIdentifierDown = 0;
                                            break;
                                        }
                                    }
                                } else if (direction == UP && progressIdentifierUp != 0) {
                                    for (int i = 0; i < (mAdapter.getItemCount() - 1); i++) {
                                        if (mAdapter.getItem(i).getIdentifier() == progressIdentifierUp) {
                                            mAdapter.remove(i);
                                            progressIdentifierUp = 0;
                                            break;
                                        }
                                    }
                                }
                            }
                        });
                    }
                }
            }
        });
    }

    private void progressItem(final ProgressState progressState, final String direction) {
        if (direction.equals(UP.toString())) {
            progressItem(progressState, UP);
        } else {
            progressItem(progressState, DOWN);
        }
    }

    /**
     * reset to default value for reload message again
     */
    private void resetMessagingValue() {
        clearAdapterItems();

        prgWaiting.setVisibility(View.VISIBLE);

        addToView = true;
        topMore = false;
        bottomMore = false;
        isWaitingForHistoryUp = false;
        isWaitingForHistoryDown = false;
        gapMessageIdUp = 0;
        gapMessageIdDown = 0;
        reachMessageIdUp = 0;
        reachMessageIdDown = 0;
        allowGetHistoryUp = true;
        allowGetHistoryDown = true;
        startFutureMessageIdUp = 0;
        startFutureMessageIdDown = 0;
        firstVisiblePosition = 0;
        visibleItemCount = 0;
        totalItemCount = 0;
        unreadCount = 0;
    }

    public class AdapterCamera extends AbstractItem<AdapterCamera, AdapterCamera.ViewHolder> {

        public String item;

        //public String getItem() {
        //    return item;
        //}

        public AdapterCamera(String item) {
            this.item = item;
        }

        //public void setItem(String item) {
        //    this.item = item;
        //}

        //The unique ID for this type of item
        @Override
        public int getType() {
            return R.id.rootCamera;
        }

        //The layout to be used for this type of item
        @Override
        public int getLayoutRes() {
            return R.layout.adapter_camera;
        }

        //The logic to bind your data to the view

        @Override
        public void unbindView(ViewHolder holder) {
            super.unbindView(holder);
        }

        @Override
        public void bindView(ViewHolder holder, List payloads) {
            super.bindView(holder, payloads);
        }

        //The viewHolder used for this item. This viewHolder is always reused by the RecyclerView so scrolling is blazing fast
        protected class ViewHolder extends RecyclerView.ViewHolder {

            CameraView cm2;
            private TextView rootCamera;

            public ViewHolder(View view) {
                super(view);

                cm2 = (CameraView) view.findViewById(R.id.cameraView);
                rootCamera = (TextView) view.findViewById(R.id.txtIconCamera);
                rootCamera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (FragmentChat.onClickCamera != null) {
                            FragmentChat.onClickCamera.onclickCamera();
                        }
                    }
                });
            }
        }

        @Override
        public ViewHolder getViewHolder(View v) {
            return new ViewHolder(v);
        }
    }

    public static void isUiThread(String name, int line) {
        if (Looper.getMainLooper().getThread() == Thread.currentThread()) {
            Log.i("UUU", name + " in line : " + line + " is UI Thread");
        } else {
            Log.i("UUU", name + " in line : " + line + " is NOT UI Thread");
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        G.fragmentActivity = (FragmentActivity) activity;
    }

    public void finishChat() {
        G.handler.post(new Runnable() {
            @Override
            public void run() {

                if (isAdded()) {
                    Fragment fragment = G.fragmentManager.findFragmentByTag(FragmentChat.class.getName());
                    removeFromBaseFragment(fragment);

                    if (G.iTowPanModDesinLayout != null) {
                        G.iTowPanModDesinLayout.onLayout(ActivityMain.chatLayoutMode.hide);
                    }
                }
            }
        });
    }

    private void closeKeyboard(View v) {
        if (isAdded()) {
            try {
                InputMethodManager imm = (InputMethodManager) G.context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            } catch (IllegalStateException e) {
                e.getStackTrace();
            }
        }
    }
}
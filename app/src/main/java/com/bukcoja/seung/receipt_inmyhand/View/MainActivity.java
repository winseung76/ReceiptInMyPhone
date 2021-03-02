package com.bukcoja.seung.receipt_inmyhand.View;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.tech.NfcF;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bukcoja.seung.receipt_inmyhand.BuildConfig;
import com.bukcoja.seung.receipt_inmyhand.DBHelper;
import com.bukcoja.seung.receipt_inmyhand.DigitalReceipt.NFCTest;
import com.bukcoja.seung.receipt_inmyhand.ListAdapter.ListViewAdapter;
import com.bukcoja.seung.receipt_inmyhand.Presenter.MainPresenter;
import com.bukcoja.seung.receipt_inmyhand.Presenter.MainTaskContact;
import com.bukcoja.seung.receipt_inmyhand.R;
import com.bukcoja.seung.receipt_inmyhand.ReceiptDecoder;
import com.bukcoja.seung.receipt_inmyhand.UserInfo;
import com.bukcoja.seung.receipt_inmyhand.VO.GroupListVO;
import com.bukcoja.seung.receipt_inmyhand.VO.GroupVO;
import com.bukcoja.seung.receipt_inmyhand.dummy.BreakfastContent;
import com.bukcoja.seung.receipt_inmyhand.dummy.DinnerContent;
import com.bukcoja.seung.receipt_inmyhand.dummy.LunchContent;
import com.bukcoja.seung.receipt_inmyhand.dummy.ReceiptContent;
import com.bukcoja.seung.receipt_inmyhand.dummy.TempContent;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ChartFragment.OnFragmentInteractionListener, ItemFragment.OnListFragmentInteractionListener, MyPointFragment.OnFragmentInteractionListener,
        MainTaskContact.View,RecommandFragment.OnListFragmentInteractionListener, BreakfastFragment.OnListFragmentInteractionListener ,LunchFragment.OnListFragmentInteractionListener,
DinnerFragment.OnListFragmentInteractionListener{
    private ChartFragment chartFragment;
    private ItemFragment itemFragment;
    private MyPointFragment myPointFragment;
    private RecommandFragment recommandFragment;
    private FragmentManager fm;
    private UserInfo userInfo;
    private PendingIntent pendingIntent;
    private IntentFilter[] filters;
    private String[][] techLists;
    NfcAdapter nfcAdapter;
    Toolbar toolbar;
    private CircleImageView profile;
    ProfilePhoto profilePhoto;
    private NavigationViewManager nvm;
    private FloationButtonManaver fbm;
    private FloatingActionButton fab, fab1, fab2,fab3;
    MainPresenter mainPresenter;
    BottomBar bottomBar;
    private FrameLayout container;
    private static final int CAMERA_PERMISSION=1;
    private static final int STORAGE_PERMISSION=2;
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE=1;
    private static final int CAPTURE_IMAGE_REQUEST = 1;
    private static final int PICK_IMAGE_REQUEST = 2;
    private static final int MY_PERMISSION_CAMERA=333;
    private String mCurrentPhotoPath;
    private Bitmap bitmap;
    private Uri imageUri;
    private static final String TAG = "MainActivity";
    private ListView listView;
    private ListViewAdapter adapter;
    NdefMessage ndefMessage;
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private Menu menu;
    private MenuItem groupOut;
    private ProgressDialog loading;

    @Override
    protected void onResume() {
        super.onResume();

        if(nfcAdapter!=null){
            nfcAdapter.enableForegroundDispatch(this, pendingIntent,filters,techLists);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        //Log.e("MainActivity","onStop");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M) {
            //requestPermissions();
            //checkPermission();
            checkVerify();
            //Log.e("onCreate","requestPermissions 호출!");
        }

        // 저장해놨던 토큰값 가져와서 전달
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        String token =  pref.getString("token", "");

        mainPresenter =new MainPresenter(this);
        mainPresenter.updateCategory(getApplicationContext());
        // 사용자의 그룹 정보 불러오기
        mainPresenter.loadGroup(getApplicationContext(),userInfo.getId());
        // 기기의 토큰을 업데이트
        mainPresenter.updateToken(getApplicationContext(),userInfo.getId(),token);
        // 서버에서 프로필 이미지 가져오기
        mainPresenter.getProfileImage(getApplicationContext(),userInfo.getId());

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                //drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                //drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            }

        };
        drawer.addDrawerListener(toggle);


        toggle.syncState();

        // NfcAdapter 클래스를 이용하여 NFC 지원/미지원을 판단할 수 있다.
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if (nfcAdapter == null) {
            // NFC 미지원단말
            Toast.makeText(getApplicationContext(), "NFC를 지원하지 않는 단말기입니다.", Toast.LENGTH_SHORT).show();
        }
        // NFC 지원하는 경우
        else{
            Toast.makeText(getApplicationContext(),"NFC를 지원함",Toast.LENGTH_SHORT).show();
        }

        // MainActivity를 띄우는 인텐트 생성
        Intent targetIntent=new Intent(this,MainActivity.class);
        // MainActivity가 액티비티 스택에 하나만 생성되도록 flag 설정
        targetIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        targetIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // pendingintent를 받으면 targetIntent를 실행하도록 한다.
        pendingIntent=PendingIntent.getActivity(this,0,targetIntent,0);

        // 인텐트 필터 객체 생성
        IntentFilter ndef=new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        try{
            ndef.addDataType("*/*");
        }catch (IntentFilter.MalformedMimeTypeException e){
            throw new RuntimeException("fail",e);
        }

        filters=new IntentFilter[]{ndef,};
        techLists = new String[][]{new String[]{NfcF.class.getName()}};

        Intent passedIntent=getIntent();
        if(passedIntent!=null){
            onNewIntent(passedIntent);
        }

        itemFragment = new ItemFragment();
        fm=getSupportFragmentManager();
        // 처음 프래그먼트로 itemFragment가 나타나도록 설정한다.
        fm.beginTransaction().replace(R.id.contentContainer,itemFragment).commit();
        manageFragment();

        Intent intent=getIntent();

        if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            Uri uri=intent.getData();
            String group = uri.getQueryParameter("group");
            //newGroupID=Integer.parseInt(group);
            //Toast.makeText(getApplicationContext(),"intent group id : "+group,Toast.LENGTH_SHORT).show();
            //Log.e("intent group id",String.valueOf(group));
            mainPresenter.isGroupExist(getApplicationContext(),userInfo.getId(),Integer.parseInt(group));
        }

    }
    public void initView(){
        userInfo=(UserInfo)getApplication();

        bottomBar = findViewById(R.id.bottombar);
        drawer=findViewById(R.id.drawer_layout);
        container=findViewById(R.id.contentContainer);

        //drawer.closeDrawer(Gravity.START);
        //drawer.setDrawerLockMode(LOCK_MODE_LOCKED_OPEN);



        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(MainActivity.this);

        menu=navigationView.getMenu().getItem(1).getSubMenu();
        MenuItem item=menu.getItem(0);
        SpannableString spanString = new SpannableString(item.getTitle().toString());
        spanString.setSpan(new ForegroundColorSpan(Color.BLACK), 0,     spanString.length(), 0); //fix the color to white
        item.setTitle(spanString);

        nvm=new NavigationViewManager();
        nvm.setNavHeader();

        fbm=new FloationButtonManaver();
        profilePhoto=new ProfilePhoto();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackground(getResources().getDrawable(R.drawable.linear_gradient));
        toolbar.setTitle("나의 영수증");
        setSupportActionBar(toolbar);
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
      /*  Log.d(this.getClass().getName(),"----------------------------");
        Log.d(this.getClass().getName(),encodedImage);
        Log.d(this.getClass().getName(),"----------------------------");*/

        return encodedImage;
    }


    /*카메라 사진찍기*/
    private void captureCamera(){
        String state= Environment.getExternalStorageState();
        //외장 메모리 검사
        if(Environment.MEDIA_MOUNTED.equals(state)){
            // 카메라 앱을 열기 위한 인텐트 객체 생성
            Intent takePictureIntent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if(takePictureIntent.resolveActivity(getPackageManager())!=null){
                File photoFile=null;
                try {
                    // 이미지 파일 생성
                    photoFile = createImageFile();
                }
                catch (Exception ex){
                    Log.e("captureCamera Error", ex.toString());
                }

                if(photoFile !=null){
                    // 카메라 앱에서 RIMP 앱으로 이미지를 보낼 수 있게 Uri을 가져옴
                    Uri providerURI= FileProvider.getUriForFile(this,getPackageName(), photoFile);

                    imageUri=providerURI;

                    //Toast.makeText(getApplicationContext(),providerURI.toString(), Toast.LENGTH_SHORT).show();

                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, providerURI);

                    // 카메라 앱으로 이동
                    startActivityForResult(takePictureIntent,1);
                }

            }
        }
        else{
            Toast.makeText(this,"저장공간이 접근불가한 기기입니다.", Toast.LENGTH_SHORT).show();
        }


        return;
    }

    // 이미지 파일 객체를 반환하기 위한 메서드
    public File createImageFile() {
        //Create an image file name

        // 파일 이름 생성
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = userInfo.getId()+"_" + timeStamp + ".jpg";

        // 파일 경로 생성
        File storageDir = new File(Environment.getExternalStorageDirectory() + "/Pictures", "receipt");

        if (!storageDir.exists()) {
            Log.i("mCurrentPhotoPath1", storageDir.toString());
            storageDir.mkdirs();
        }

        // 파일 이름, 파일 경로 정보를 통해 File 객체 생성
        File imageFile = new File(storageDir, imageFileName);
        mCurrentPhotoPath = imageFile.getAbsolutePath();

        return imageFile;
    }

    String getFileName(Uri uri){
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }
    @Override
    public void setPresenter(MainTaskContact.Presenter presenter) {
        this.mainPresenter =(MainPresenter)presenter;
    }

    @Override
    public void getImageUploadResult(boolean success) {

        if(loading!=null && loading.isShowing())
            loading.dismiss();

        if(success){
            Toast.makeText(getApplicationContext(),"사진이 업로드 되었습니다.",Toast.LENGTH_SHORT).show();

            // 사진을 변경하면 바로 변경된 사진이 반영되도록
            mainPresenter.getProfileImage(getApplicationContext(),userInfo.getId());
        }
        else{
            Toast.makeText(getApplicationContext(),"업로드에 실패하였습니다.",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void setDefaultProfileImage(boolean success) {
        if(success){
            //profile.setImageDrawable(getResources().getDrawable(R.drawable.defaultprofile));
            mainPresenter.getProfileImage(getApplicationContext(),userInfo.getId());
        }
    }

    @Override
    public void getProfileUploadResult(boolean success) {
        if(loading!=null && loading.isShowing())
            loading.dismiss();
        if(success){
            Toast.makeText(getApplicationContext(),"사진 변경 완료",Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(getApplicationContext(),"사진 변경 실패",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void setProfileImage(String imageUri) {

        Log.e("setProfileImage",imageUri);

        if(imageUri!=null){
            Picasso.with(getApplicationContext())
                    .load(imageUri)
                    .into(profile);

        }
        /*
        if(imageVO!=null) {
            Toast.makeText(getApplicationContext(),imageVO.getImage(),Toast.LENGTH_SHORT).show();
            Bitmap bitmap = BitmapFactory.decodeFile(imageVO.getImage());
            profile.setImageBitmap(bitmap);
        }
        */
        else
            Toast.makeText(getApplicationContext(),"imageVO is null",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setReceiptSaveResult(boolean success) {
        // 성공적으로 전자영수증 저장이 완료된 경우
        if(success){

            if(itemFragment.isVisible()){

                //Toast.makeText(getApplicationContext(),"전자 영수증 저장 성공",Toast.LENGTH_SHORT).show();
                fm.beginTransaction().detach(itemFragment).attach(itemFragment).commit();
                //fm.beginTransaction().show(itemFragment).commit();
            }

        }
        else
            Toast.makeText(getApplicationContext(),"전자 영수증 저장 실패",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showGroupResultDialog(String result) {
        String msg;

        if(result==null){
            msg="그룹 생성이 실패하였습니다.\n다시 시도하세요.";
        }
        else {
            if (result.contains("1"))
                msg = "그룹이 성공적으로 생성되었습니다.";
            else
                msg = "그룹 생성이 실패하였습니다.\n다시 시도하세요.";
        }

        //Log.e("showGroupResultDialog",result);

        AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);

        View contentView=View.inflate(getApplicationContext(), R.layout.dialog_with_one_button, null);
        builder.setView(contentView);

        TextView message=contentView.findViewById(R.id.msg);
        TextView ok=contentView.findViewById(R.id.ok);
        message.setText(msg);

        final AlertDialog dialog=builder.create();
        dialog.show();

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainPresenter.loadGroup(getApplicationContext(),userInfo.getId());
                dialog.dismiss();
            }
        });
    }

    @Override
    public void setUserGroup(GroupListVO groupVO) {

        int size=menu.size();
        userInfo.removeAllGroups();

        for(int i=0;i<size;i++){
            menu.removeItem(0);
        }

        //menu.clear();
        Log.e("original group size",String.valueOf(menu.size()));
        Log.e("group size",String.valueOf(groupVO.size()));

        for(int i=0;i<groupVO.size();i++){
            final int groupid=groupVO.get(i).getGroupid();
            final String groupname=groupVO.get(i).getGroupname();

            MenuItem item=menu.add(groupname);
            //Log.e("menu size",String.valueOf(menu.size()));
            Log.e("groupVO id",String.valueOf(groupVO.get(i).getGroupid()));
            item.setIcon(R.drawable.group);

            userInfo.addGroups(groupname);
            userInfo.addGroupIds(groupid);
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

            item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                    groupOut.setVisible(true);

                    bottomBar.selectTabWithId(R.id.tab_list);

                    //Log.e("getCurrentTab",String.valueOf( bottomBar.getTabWithId(R.id.tab_list).isActivated()));
                    try {
                        userInfo.setGroup(groupid);
                        userInfo.setGroupname(groupname);
                        drawer.closeDrawer(GravityCompat.START);

                        // itemFragment가 null 인 경우 == itemFragment가 처음 로드되는 경우
                        if(itemFragment == null) {
                            itemFragment=new ItemFragment();
                            // itemFragment를 fm에 추가한다.
                            fm.beginTransaction().add(R.id.contentContainer, itemFragment).commit();
                        }
                        else {
                            fm.beginTransaction().detach(itemFragment).attach(itemFragment).commit();
                            fm.beginTransaction().show(itemFragment).commit();
                        }

                        if(chartFragment!=null) {
                            fm.beginTransaction().detach(chartFragment);
                            fm.beginTransaction().remove(chartFragment);
                            chartFragment=null;
                        }
                        if(myPointFragment!=null) {
                            fm.beginTransaction().detach(myPointFragment);
                            fm.beginTransaction().remove(myPointFragment);
                            myPointFragment=null;
                        }
                        if(recommandFragment!=null) {
                            fm.beginTransaction().detach(recommandFragment);
                            fm.beginTransaction().remove(recommandFragment);
                            recommandFragment=null;
                        }
                        /*
                        if(chartFragment!=null)
                            fm.beginTransaction().hide(chartFragment).commit();
                        if(myPointFragment!=null)
                            fm.beginTransaction().hide(myPointFragment).commit();
                        if(recommandFragment!=null)
                            fm.beginTransaction().hide(recommandFragment).commit();
                            */

                        toolbar.setTitle(userInfo.getGroupname());

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    return true;
                }
            });
        }

    }

    // 카카오톡으로 초대를 받았을 때 초대받은 그룹에 이미 포함되어있는지 여부의 결과를 받아옴
    @Override
    public void getGroupExistResult(GroupVO groupVO) {

        Log.e("getGroupExistResult",String.valueOf(groupVO.getGroupid()));

        //Toast.makeText(getApplicationContext(),"그룹 이름: "+groupVO.getGroupname(),Toast.LENGTH_SHORT).show();

        // 그룹에 포함되지 않은 경우
        if(groupVO.getGroupid()>0){
            // 그룹 추가에 동의하는 대화상자 띄우기
            showGroupInviteDialog(groupVO);
        }
        // 그룹에 이미 포함되어 있는 경우
        else{
            // 이미 포함되어 있다는 대화상자 띄우기
            showGroupExistAlertDialog();
        }
    }

    // 그룹 삭제에 대한 결과를 받아옴
    @Override
    public void getGroupRemoveResult(boolean success) {
        if(success){
            userInfo.setGroup(1);
            userInfo.setGroupname("나의 영수증");
            groupOut.setVisible(false);

            if(itemFragment == null) {
                itemFragment=new ItemFragment();
                // itemFragment를 fm에 추가한다.
                fm.beginTransaction().add(R.id.contentContainer, itemFragment).commit();
            }
            else {
                fm.beginTransaction().detach(itemFragment).attach(itemFragment).commit();
                fm.beginTransaction().show(itemFragment).commit();
            }
            toolbar.setTitle(userInfo.getGroupname());
            bottomBar.selectTabWithId(R.id.tab_list);

            mainPresenter.loadGroup(getApplicationContext(),userInfo.getId());

        }
        //Toast.makeText(getApplicationContext(),String.valueOf(success),Toast.LENGTH_SHORT).show();
    }

    public void showGroupExistAlertDialog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        final View dialogView= View.inflate(getApplicationContext(), R.layout.dialog_with_one_button, null);
        builder.setView(dialogView);
        TextView msg=dialogView.findViewById(R.id.msg);
        msg.setText("해당 그룹이 이미 존재합니다.");
        TextView ok=dialogView.findViewById(R.id.ok);

        final AlertDialog dialog=builder.create();

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
    public void showGroupInviteDialog(final GroupVO groupVO){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        final View dialogView= View.inflate(getApplicationContext(), R.layout.dialog_with_two_button, null);
        builder.setView(dialogView);
        TextView msg=dialogView.findViewById(R.id.msg);
        msg.setText(groupVO.getGroupname()+" 을 추가하시겠습니까?");
        TextView yes=dialogView.findViewById(R.id.yes);
        TextView no=dialogView.findViewById(R.id.no);

        final AlertDialog dialog=builder.create();

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainPresenter.registerGroup(getApplicationContext(),userInfo.getId(),groupVO.getGroupid(),groupVO.getGroupname());
                dialog.dismiss();
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
    public void createPhotoSelectDialog(){
        LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
        View dialogView=inflater.inflate(R.layout.photo_select_dialog,null);
        ImageButton defaultImage=dialogView.findViewById(R.id.defaultImage);

        AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("사진을 선택하세요.");
        builder.setView(dialogView);

        final Dialog dialog=builder.create();
        dialog.show();
        defaultImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profilePhoto.setDefaultImage();
                dialog.cancel();
            }
        });
        ImageButton album=dialogView.findViewById(R.id.album);
        album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profilePhoto.goToAlbum();
                dialog.cancel();
            }
        });
    }
    public void createLogoutDialog(){
        //AlertDialog dialog=new AlertDialog(getApplicationContext());
        AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("로그아웃 하시겠습니까?");
        View contentView=View.inflate(getApplicationContext(), R.layout.logout_dialog, null);

        TextView yes=contentView.findViewById(R.id.yes);
        TextView no=contentView.findViewById(R.id.no);

        builder.setView(contentView);

        final AlertDialog dialog=builder.create();
        dialog.show();

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this, LoginActivity.class);
                intent.putExtra("id",userInfo.getId());
                intent.putExtra("state",0);
                startActivity(intent);
                dialog.cancel();
                finish();
                //dbHelper.insert("update login set state="+0+" where id='"+userInfo.getId()+"';");
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });

    }
    public void createGroupOutDialog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
        View dialogView=View.inflate(getApplicationContext(),R.layout.dialog_with_two_button,null);
        builder.setView(dialogView);

        TextView msg=dialogView.findViewById(R.id.msg);
        TextView yes=dialogView.findViewById(R.id.yes);
        TextView no=dialogView.findViewById(R.id.no);

        final AlertDialog dialog=builder.create();
        dialog.show();

        msg.setText(userInfo.getGroupname()+" 그룹에서 나가시겠습니까?");
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainPresenter.removeGroup(getApplicationContext(),userInfo.getId(),userInfo.getGroup());
                dialog.dismiss();
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Uri filePath=null;

        if (resultCode == RESULT_OK) {
            if (requestCode == PICK_IMAGE_REQUEST && data != null && data.getData() != null) {
                // 앨범으로부터 가져온 이미지 경로를 저장
                filePath = data.getData();
                try {

                    // 앨범에서 가져온 이미지를 비트맵 형식으로 저장
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);

                    // 앨범에서 가져온 이미지를 서버에 저장 중이라는 로딩 메시지 화면에 띄우기
                    loading = ProgressDialog.show(this, null, "사진을 서버로 전송중입니다.\n잠시만 기다려주세요.",
                            true,true);

                    // Presenter를 통해 이미지를 서버로 보냄
                    mainPresenter.uploadImage(getApplicationContext(),getStringImage(bitmap),getFileName(filePath),userInfo.getId());

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            else if (requestCode == CAPTURE_IMAGE_REQUEST) {

                // 카메라로 찍은 사진을 앨범에 저장
                galleryAddPic();

                filePath=imageUri;

                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);

                } catch (IOException e){
                    e.printStackTrace();
                }
                Log.e("getStringimage bitmap",getStringImage(bitmap));
                Log.e("filePath",getFileName(filePath));

                // 사진을 서버로 보내는 동안 로딩 메시지를 화면에 띄우기
                loading = ProgressDialog.show(this, null, "사진을 서버로 전송중입니다.\n잠시만 기다려주세요.",
                        true,true);

                // Presenter를 통해 이미지를 서버로 보냄
                mainPresenter.uploadImage(getApplicationContext(),getStringImage(bitmap),getFileName(filePath),userInfo.getId());

            }
            else if(data==null){
                //Toast.makeText(getApplicationContext(), "data에 문제 생김!", Toast.LENGTH_SHORT);
            }
            else
                profilePhoto.activityResult(requestCode,resultCode,data);
        }
    }
    /*카메라로 찍은 사진을 앨범에 저장 */
    public void galleryAddPic(){

        //Log.d("galleryAddPic", "Call");
        Intent mediaScanIntent=new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);

        //해당 경로에 있는 파일 객체화 (새로 파일 생성)
        File f=new File(mCurrentPhotoPath);
        Uri contentUri=Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        Log.d("tag",contentUri.toString());

        // 브로드캐스팅을 통해 해당 경로의 파일을 앨범에 저장
        sendBroadcast(mediaScanIntent);
        Toast.makeText(this, "사진이 앨범에 저장됨.", Toast.LENGTH_SHORT).show();

    }

    private void showFileChooser() {
        // 인텐트 생성
        Intent intent = new Intent();
        // 인텐트의 타입 설정
        intent.setType("image/*");
        // 인텐트 액션 설정
        intent.setAction(Intent.ACTION_GET_CONTENT);
        // 인텐트 호출 및 호출에 대한 결과를 받아옴
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }
    public void checkVerify() {

        if (ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                        ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.WRITE_EXTERNAL_STORAGE))
            {
                new AlertDialog.Builder(this)
                        .setTitle("알림")
                        .setMessage("저장소 권한이 거부되었습니다. 사용을 원하시면 권한을 허용하세요.")
                        .setNeutralButton("설정", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                intent.setData(Uri.parse("package:" + getPackageName()));
                                startActivity(intent);
                            }
                        })
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                            }

                        })
                        .setCancelable(false)
                        .create()
                        .show();
            }
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE},
                    1);

        }

        /*
        String[] permissions=new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE};

        for(String permission:permissions){
            int result= PermissionChecker.checkSelfPermission(this,permission);
            if(result==PermissionChecker.PERMISSION_GRANTED){

            }
            else{
                doRequestPermission();
            }
        }
        */
    }
    private void checkCameraVerify(){
        // 카메라 권한이 부여되지 않은 경우
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // 카메라 권한을 요청하는 대화상자를 띄움
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.CAMERA)) {
                new AlertDialog.Builder(this)
                        .setTitle("알림")
                        .setMessage("저장소 권한이 거부되었습니다. 사용을 원하시면 권한을 허용하세요.")
                        .setNeutralButton("설정", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                intent.setData(Uri.parse("package:" + getPackageName()));
                                startActivity(intent);
                            }
                        })
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                            }
                        })
                        .setCancelable(false)
                        .create()
                        .show();
            }
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA}, MY_PERMISSION_CAMERA);

        }
        else    // 카메라 권한이 부여된 경우
            captureCamera();
    }


    private void doRequestPermission(){
        String[] permissions=new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE};

        ArrayList<String> notGrantedPermissions=new ArrayList<>();
        for(String perm:permissions){
            //if(!PermissionUtils.hasPermissions(this,perm))
        }
    }


    private void startPermissionRequest(int perm) {
        switch (perm){
            case CAMERA_PERMISSION:
                ActivityCompat.requestPermissions(this,
                        new String[] {Manifest.permission.CAMERA} , REQUEST_PERMISSIONS_REQUEST_CODE);
                break;
            case STORAGE_PERMISSION:
                ActivityCompat.requestPermissions(this,
                        new String[] {Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE} , REQUEST_PERMISSIONS_REQUEST_CODE);
                //ActivityCompat.requestPermissions(this,
                //        new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE} , REQUEST_PERMISSIONS_REQUEST_CODE);
                break;
        }

    }

    private void checkPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            if((ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)) ||
                    (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA))) {
                new AlertDialog.Builder(this)
                        .setTitle("알림")
                        .setMessage("저장소 권한이 거부되었습니다. 사용을 원하시면 권한을 허용하세요.")
                        .setNeutralButton("설정", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                intent.setData(Uri.parse("package:" + getPackageName()));
                                startActivity(intent);
                            }
                        })
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                            }

                        })
                        .setCancelable(false)
                        .create()
                        .show();
            }
            else{
                ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, MY_PERMISSION_CAMERA);

            }
        }
    }

    private void requestPermissions() {

        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){

            Log.e("requestPermission","READ WRITE NOT OK");

            boolean shouldProviceRationale =
                    ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE);

            if(shouldProviceRationale){
                new android.support.v7.app.AlertDialog.Builder(this)
                        .setTitle("알림")
                        .setMessage("저장소 권한이 필요합니다.")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startPermissionRequest(STORAGE_PERMISSION);
                            }
                        })
                        .create()
                        .show();

            }
            else {
                Log.e("requestPermission", "shouldProviceRationale is false");

                boolean shouldProviceRationale2 = ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE);

                if(shouldProviceRationale2){
                    new android.support.v7.app.AlertDialog.Builder(this)
                            .setTitle("알림")
                            .setMessage("저장소 권한이 필요합니다.")
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    startPermissionRequest(STORAGE_PERMISSION);
                                }
                            })
                            .create()
                            .show();

                }
            }

        }
        else
            Log.e("requestPermission","READ WRITE OK");

        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ){
            boolean shouldProviceRationale =
                    ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.CAMERA);

            if(shouldProviceRationale) {
                new android.support.v7.app.AlertDialog.Builder(this)
                        .setTitle("알림")
                        .setMessage("카메라 권한이 필요합니다.")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startPermissionRequest(CAMERA_PERMISSION);
                            }
                        })
                        .create()
                        .show();
            }
        }
        else
            Log.e("requestPermission","CAMERA OK");
        /*
        boolean shouldProviceRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.READ_EXTERNAL_STORAGE);
        boolean shouldProviceRationale2 =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.CAMERA);

        boolean shouldProviceRationale3=ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if(shouldProviceRationale) {
            new android.support.v7.app.AlertDialog.Builder(this)
                    .setTitle("알림")
                    .setMessage("저장소 권한이 필요합니다.")
                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startPermissionRequest(STORAGE_PERMISSION);
                        }
                    })
                    .create()
                    .show();
        }
        if(shouldProviceRationale2) {
            new android.support.v7.app.AlertDialog.Builder(this)
                    .setTitle("알림")
                    .setMessage("카메라 권한이 필요합니다.")
                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startPermissionRequest(CAMERA_PERMISSION);
                        }
                    })
                    .create()
                    .show();
        }
        */
        /*
        if(shouldProviceRationale3) {
            new android.support.v7.app.AlertDialog.Builder(this)
                    .setTitle("알림")
                    .setMessage("저장소 쓰기 권한이 필요합니다.")
                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startPermissionRequest(STORAGE_PERMISSION);
                        }
                    })
                    .create()
                    .show();
        }
        */
        /*
        else {
            startPermissionRequest(STORAGE_PERMISSION);
            startPermissionRequest(CAMERA_PERMISSION);
        }
        */
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSIONS_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    new android.support.v7.app.AlertDialog.Builder(this)
                            .setTitle("알림")
                            .setMessage("저장소 권한이 필요합니다. 환경 설정에서 저장소 권한을 허가해주세요.")
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent();
                                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    Uri uri = Uri.fromParts("package",
                                            BuildConfig.APPLICATION_ID, null);
                                    intent.setData(uri);
                                    intent.setFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                }
                            })
                            .create()
                            .show();
                }
                break;
            case MY_PERMISSION_CAMERA:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    captureCamera();
                }
                else {
                    new android.support.v7.app.AlertDialog.Builder(this)
                            .setTitle("알림")
                            .setMessage("저장소 권한이 필요합니다. 환경 설정에서 저장소 권한을 허가해주세요.")
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent();
                                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    Uri uri = Uri.fromParts("package",
                                            BuildConfig.APPLICATION_ID, null);
                                    intent.setData(uri);
                                    intent.setFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                }
                            })
                            .create()
                            .show();
                }
        }
    }
    class ProfilePhoto{
        private File tempFile;
        private static final int PICK_FROM_ALBUM = 100;
        private static final int PICK_FROM_CAMERA = 200;
        private static final int CROP_FROM_IMAGE=300;
        private Uri photoUri;

        ProfilePhoto(){

        }

        private void goToAlbum() {

            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
            startActivityForResult(intent, PICK_FROM_ALBUM);
        }
        public void setDefaultImage(){
            //profile.setImageDrawable(getResources().getDrawable(R.drawable.defaultprofile));
            mainPresenter.changeDefaultImage(getApplicationContext(),userInfo.getId());
        }
        private void takePhoto() {

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            try {
                tempFile = createImageFile();
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "이미지 처리 오류! 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                finish();
                e.printStackTrace();
            }
            if (tempFile != null) {

                photoUri = Uri.fromFile(tempFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(intent, PICK_FROM_CAMERA);
            }
            else
                Log.e("tempFile","null");
        }
        private File createImageFile() throws IOException {

            // 이미지 파일 이름 ( rimp_{시간}_ )
            String timeStamp = new SimpleDateFormat("HHmmss").format(new Date());
            String imageFileName = "rimp_" + timeStamp + "_";

            // 이미지가 저장될 폴더 이름 ( blackJin )
            File storageDir = new File(Environment.getExternalStorageDirectory() + "/rimp/");
            if (!storageDir.exists()) storageDir.mkdirs();

            // 빈 파일 생성
            File image = File.createTempFile(imageFileName, ".jpg", storageDir);

            return image;
        }
        private void setImage() {

            BitmapFactory.Options options = new BitmapFactory.Options();
            Bitmap originalBm = BitmapFactory.decodeFile(tempFile.getAbsolutePath(), options);

            profile.setImageBitmap(originalBm);
        }

        public void activityResult(int requestCode, int resultCode,Intent data){

            if (resultCode != Activity.RESULT_OK) {
                Toast.makeText(MainActivity.this, "취소 되었습니다.", Toast.LENGTH_SHORT).show();
                if(tempFile != null) {
                    if (tempFile.exists()) {
                        if (tempFile.delete()) {
                            Log.e(TAG, tempFile.getAbsolutePath() + " 삭제 성공");
                            tempFile = null;
                        }
                    }
                }
                return;
            }

            switch (requestCode){
                case PICK_FROM_ALBUM:
                    // 갤러리에서 선택한 이미지의 uri를 받아옴
                    photoUri = data.getData();
                    Cursor cursor = null;
                    try {

                        //Uri 스키마를 content:/// 에서 file:/// 로  변경한다.
                        String[] proj = { MediaStore.Images.Media.DATA };

                        assert photoUri != null;
                        cursor = getContentResolver().query(photoUri, proj, null, null, null);
                        assert cursor != null;
                        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

                        cursor.moveToFirst();
                        tempFile = new File(cursor.getString(column_index));

                    } finally {
                        if (cursor != null) {
                            cursor.close();
                        }
                    }
                    //setImage();
                case PICK_FROM_CAMERA:
                    Intent intent=new Intent("com.android.camera.action.CROP");
                    intent.setDataAndType(photoUri,"image/*");
                    intent.putExtra("outputX",100);
                    intent.putExtra("outputY",100);
                    intent.putExtra("aspectX",1);
                    intent.putExtra("aspectY",1);
                    intent.putExtra("scale",true);
                    intent.putExtra("return-data",true);
                    startActivityForResult(intent,CROP_FROM_IMAGE);
                    break;
                    //setImage();
                case CROP_FROM_IMAGE:
                    if(resultCode!=RESULT_OK)
                        return;

                    final Bundle extra=data.getExtras();
                    if(extra!=null){
                        Bitmap photo=extra.getParcelable("data");
                        profile.setImageBitmap(photo);
                        /*
                        profile.setBackground(new ShapeDrawable(new OvalShape()));
                        if(Build.VERSION.SDK_INT >= 21) {
                            profile.setClipToOutline(true);
                        }
                        */
                        mainPresenter.uploadProfileImage(getApplicationContext(),getStringImage(photo),getFileName(photoUri),userInfo.getId());
                    }
                    break;
            }

        }
    }


    // 액티비티가 이미 액티비티 스택에 있는데 인텐트를 전달받은 경우 호출됨
    public void onNewIntent(Intent passedIntent){
        Log.d(TAG,"onNewIntent() called.");

        if(passedIntent!=null){
            //onNewIntent(passedIntent);
            String action=passedIntent.getAction();
            //Toast.makeText(getApplicationContext(),action,Toast.LENGTH_SHORT).show();
            //processTag(passedIntent);


            if(NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)){
                //Toast.makeText(getApplicationContext(),"ACTION_TAG_DISCOVERED",Toast.LENGTH_SHORT).show();
                processTag(passedIntent);
            }

            if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
                //Toast.makeText(getApplicationContext(),"ACTION_NDEF_DISCOVERED",Toast.LENGTH_SHORT).show();
                processTag(passedIntent);
            }

        }
    }
    public void processTag(Intent passedIntent){
        Log.d(TAG,"processTag() called");

        Parcelable[] rawMsgs=passedIntent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);

        if(rawMsgs==null){
            Log.d(TAG,"NDEF is null.");
            return;
        }

        //Toast.makeText(getApplicationContext(),rawMsgs.length+"개 태그 스캔됨",Toast.LENGTH_SHORT).show();

        NdefMessage[] msgs;

        if(rawMsgs!=null) {
            msgs = new NdefMessage[rawMsgs.length];
            for (int i = 0; i < rawMsgs.length; i++) {
                msgs[i] = (NdefMessage) rawMsgs[i];
            }
            // 포스기로부터 받은 NdefMessage를 디코딩하는 과정
            //Toast.makeText(getApplicationContext(), String.valueOf(msgs[0].getByteArrayLength()), Toast.LENGTH_SHORT).show();
            // Log.e("ndefmessage byte length",String.valueOf(msgs[0].getByteArrayLength()));
            //Toast.makeText(getApplicationContext(), DataChanger.byteArrayToBinaryString(msgs[0].toByteArray()), Toast.LENGTH_SHORT).show();

            DBHelper dbHelper=new DBHelper(getApplicationContext(),"rimp.db",null,3);
            new ReceiptDecoder(getApplicationContext(),dbHelper,userInfo,mainPresenter).decode(msgs[0]);
        }
    }
    @Override
    protected void onPause() {
        super.onPause();

        if(nfcAdapter!=null){
            nfcAdapter.disableForegroundDispatch(this);
        }
    }

    public void manageFragment(){

        bottomBar.setTabTitleTextAppearance(R.style.bottomBarTextView);
        bottomBar.setActiveTabColor(getResources().getColor(R.color.colorPrimary));
        bottomBar.getTabAtPosition(2).setInActiveColor(ContextCompat.getColor(this, R.color.colorPrimary));
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            // 하단바의 아이콘들을 각각 선택했을 떄의 리스너
            @Override
            public void onTabSelected(int tabId) {

                switch (tabId) {
                    case R.id.tab_list:
                        // itemFragment가 null 인 경우 == itemFragment가 처음 로드되는 경우
                        if(itemFragment == null) {
                            itemFragment=new ItemFragment();
                            // itemFragment를 fm에 추가한다.
                            fm.beginTransaction().add(R.id.contentContainer, itemFragment).commit();
                        }
                        else
                            // 전에 itemFragment를 띄운 적이 있는 경우, 다시 보여주는 show() 메소드를 호출한다.
                            // 이는 다른 프래그먼트로 갔다가 왔을 때 전의 상태를 유지시키기 위해서 replace()대신에 show()와 hide()를 사용
                            // show()를 사용하는 경우 프래그먼트의 onCreateView()가 다시 호출되지 않는다.
                            fm.beginTransaction().show(itemFragment).commit();

                        if(chartFragment!=null)
                            fm.beginTransaction().hide(chartFragment).commit();
                        if(myPointFragment!=null)
                            fm.beginTransaction().hide(myPointFragment).commit();
                        if(recommandFragment!=null)
                            fm.beginTransaction().hide(recommandFragment).commit();

                        //toolbar.setTitle("나의 영수증");
                        toolbar.setTitle(userInfo.getGroupname());
                        break;
                    case R.id.tab_chart:
                        //transaction.replace(R.id.contentContainer, chartFragment).commit();
                        if(chartFragment == null) {
                            chartFragment=new ChartFragment();
                            fm.beginTransaction().add(R.id.contentContainer, chartFragment).commit();
                        }
                        else
                            fm.beginTransaction().show(chartFragment).commit();

                        if(itemFragment!=null)
                            fm.beginTransaction().hide(itemFragment).commit();
                        if(myPointFragment!=null)
                            fm.beginTransaction().hide(myPointFragment).commit();
                        if(recommandFragment!=null)
                            fm.beginTransaction().hide(recommandFragment).commit();

                        //toolbar.setTitle("소비 차트");
                        toolbar.setTitle(userInfo.getGroupname());
                        //alarmFragment.dbinput.cancel(true);
                        break;
                    case R.id.tab_mypoint:
                        //transaction.replace(R.id.contentContainer, myPointFragment).commit();
                        if(myPointFragment == null) {
                            myPointFragment=new MyPointFragment();
                            fm.beginTransaction().add(R.id.contentContainer, myPointFragment).commit();
                        }
                        else
                            fm.beginTransaction().show(myPointFragment).commit();
                        if(itemFragment!=null)
                            fm.beginTransaction().hide(itemFragment).commit();
                        if(chartFragment!=null)
                            fm.beginTransaction().hide(chartFragment).commit();
                        if(recommandFragment!=null)
                            fm.beginTransaction().hide(recommandFragment).commit();

                        toolbar.setTitle("나의 포인트");
                        break;
                    case R.id.tab_add:
                        // 다이얼로그 보여주기
                        //createDialog();
                        break;
                    case R.id.tab_home:
                        if(recommandFragment == null) {
                            recommandFragment=new RecommandFragment();
                            fm.beginTransaction().add(R.id.contentContainer, recommandFragment).commit();
                        }
                        else
                            fm.beginTransaction().show(recommandFragment).commit();
                        if(itemFragment!=null)
                            fm.beginTransaction().hide(itemFragment).commit();
                        if(chartFragment!=null)
                            fm.beginTransaction().hide(chartFragment).commit();
                        if(myPointFragment!=null)
                            fm.beginTransaction().hide(myPointFragment).commit();

                        toolbar.setTitle("RIMP 맛집 결정");
                        break;
                }
                //toolbar.setTitle(userInfo.getGroupname());
                if(fbm.getFabState())
                    fbm.anim();

            }
        });
    }

    public void createNFCDialog(){

        AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
        View contentView=View.inflate(getApplicationContext(), R.layout.nfc_dialog, null);
        Button cancel=contentView.findViewById(R.id.cancel);
        ImageView image=contentView.findViewById(R.id.image);
        Button encode=contentView.findViewById(R.id.encode);
        final Button decode=contentView.findViewById(R.id.decode);


        builder.setView(contentView);

        final AlertDialog dialog=builder.create();

        dialog.show();

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        encode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                byte[] data=new NFCTest().encoding();
                //byte[] compressData= DataChanger.byteCompress(data);

                Log.e("before compress",String.valueOf(data.length));
                Log.e("after compress",String.valueOf(data.length));

                // NDEF 메시지로 인코딩
                String mimeType="application/x-receipt";
                NdefRecord ndefRecord=new NdefRecord(NdefRecord.TNF_WELL_KNOWN,mimeType.getBytes(Charset.forName("utf-8")),new byte[0],data);
                ndefMessage=new NdefMessage(ndefRecord);

                /*
                String str="";
                for(int i=0;i<data.length;i++){
                    str+= DataChanger.byteToBinaryString(data[i]);
                }
                */
            }
        });
        decode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

                //Toast.makeText(getApplicationContext(),"decode group id : "+userInfo.getGroup(),Toast.LENGTH_SHORT).show();
                DBHelper dbHelper=new DBHelper(getApplicationContext(), "rimp.db", null, 3);
                ReceiptDecoder decoder=new ReceiptDecoder(getApplicationContext(),dbHelper,userInfo,mainPresenter);
                decoder.decode(ndefMessage);

                Toast.makeText(getApplication(),"전자 영수증 발급 완료",Toast.LENGTH_SHORT).show();

            }
        });
    }

    // 현재 보여지고 있는 프래그먼트를 가져오는 메소드
    public Fragment getVisibleFragment() {
        for (Fragment fragment: fm.getFragments()) {
            if (fragment.isVisible()) {
                return ((Fragment)fragment);
            }
        }
        return null;
    }
    public void onFragmentInteraction(Uri uri) { }

    @Override
    public void onListFragmentInteraction(ReceiptContent.DummyItem item) {

    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        groupOut = menu.findItem(R.id.group_out);
        groupOut.setVisible(false);
        return true;
    }

    // 툴바 메뉴 선택시 반응을 처리하는 메소드
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        // refresh 메뉴를 선택한 경우
        // 현재 보여지고 있는 프래그먼트를 refresh해준다.
        if (id == R.id.refresh) {
            Fragment visible=getVisibleFragment();
            if(visible!=null){
                fm.beginTransaction().detach(visible).attach(visible).commit();
            }
            return true;
        }
        else if(id==R.id.settings){
            Intent intent=new Intent(getApplicationContext(), SettingActivity.class);
            startActivity(intent);

            return true;
        }
        else if(id==R.id.group_out){
            createGroupOutDialog();
        }

        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        int id = item.getItemId();

        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);
        //drawer.setDrawerLockMode(LOCK_MODE_LOCKED_OPEN);

        if (id == R.id.nav_my_receipt) {
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            groupOut.setVisible(false);

            userInfo.setGroup(1);
            userInfo.setGroupname("나의 영수증");
            drawer.closeDrawer(GravityCompat.START);

            if(itemFragment == null) {
                itemFragment=new ItemFragment();
                // itemFragment를 fm에 추가한다.
                fm.beginTransaction().add(R.id.contentContainer, itemFragment).commit();
            }
            else {
                fm.beginTransaction().detach(itemFragment).attach(itemFragment).commit();
                fm.beginTransaction().show(itemFragment).commit();
            }

            if(chartFragment!=null) {
                fm.beginTransaction().detach(chartFragment);
                chartFragment=null;
            }
            if(myPointFragment!=null) {
                fm.beginTransaction().detach(myPointFragment);
                myPointFragment=null;
            }
            if(recommandFragment!=null) {
                fm.beginTransaction().detach(recommandFragment);
                recommandFragment=null;
            }
            /*
            if(chartFragment!=null)
                fm.beginTransaction().hide(chartFragment).commit();
            if(myPointFragment!=null)
                fm.beginTransaction().hide(myPointFragment).commit();
            if(recommandFragment!=null)
                fm.beginTransaction().hide(recommandFragment).commit();
                */

            bottomBar.selectTabWithId(R.id.tab_list);

            //toolbar.setTitle(userInfo.getGroupname());
        }
        else if (id == R.id.nav_group_receipt) {
            AlertDialog.Builder builer=new AlertDialog.Builder(MainActivity.this);
            View contentView=View.inflate(getApplicationContext(), R.layout.create_group_dialog, null);

            TextView cancel=contentView.findViewById(R.id.cancel);
            TextView create=contentView.findViewById(R.id.create);

            final EditText groupname=contentView.findViewById(R.id.groupname);
            ImageButton deleteBtn=contentView.findViewById(R.id.delete);
            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    groupname.setText("");
                }
            });

            builer.setView(contentView);
            builer.setTitle("그룹 생성");
            final AlertDialog dialog=builer.create();
            dialog.show();
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

                }
            });
            create.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String group=groupname.getText().toString();

                    //MenuItem item=menu.add(group);
                    //item.setIcon(R.drawable.group);

                    //userInfo.addGroups(group);

                    dialog.dismiss();
                    mainPresenter.registerGroup(getApplicationContext(),userInfo.getId(),-1,group);
                    drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                }
            });
        }
        toolbar.setTitle(userInfo.getGroupname());

        return true;
    }
    class FloationButtonManaver implements View.OnClickListener {

        private Boolean isFabOpen = false;
        private Animation fab_open, fab_close,fab_rotate,fab_rotate_reverse;
        private FrameLayout frameLayout;


        FloationButtonManaver(){

            frameLayout=findViewById(R.id.bottom);
            fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
            fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
            fab_rotate=AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_rotate);
            fab_rotate_reverse=AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_rotate_reverse);

            fab = findViewById(R.id.fab);
            fab1 = findViewById(R.id.fab_nfc);
            fab2 = findViewById(R.id.fab_camera);
            fab3=findViewById(R.id.fab_album);

            fab.setOnClickListener(this);
            fab1.setOnClickListener(this);
            fab2.setOnClickListener(this);
            fab3.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int id = view.getId();
            switch (id) {
                case R.id.fab:
                    anim();
                    break;
                case R.id.fab_nfc:
                    anim();
                    createNFCDialog();
                    break;
                case R.id.fab_camera:
                    anim();
                    //checkPermission();
                    //checkVerify();
                    //captureCamera();
                    checkCameraVerify();
                    break;
                case R.id.fab_album:
                    anim();
                    showFileChooser();
                    break;
            }
        }

        public boolean getFabState(){return isFabOpen;}
        public void anim() {

            if (isFabOpen) {

                fab1.startAnimation(fab_close);
                fab2.startAnimation(fab_close);
                fab3.startAnimation(fab_close);
                fab.startAnimation(fab_rotate_reverse);
                frameLayout.setBackgroundColor(Color.parseColor("#00000000"));
                fab1.setClickable(false);
                fab2.setClickable(false);
                fab3.setClickable(false);
                isFabOpen = false;
            } else {

                fab1.startAnimation(fab_open);
                fab2.startAnimation(fab_open);
                fab3.startAnimation(fab_open);
                fab.startAnimation(fab_rotate);
                frameLayout.setBackgroundColor(Color.parseColor("#80000000"));
                fab1.setClickable(true);
                fab2.setClickable(true);
                fab3.setClickable(true);
                isFabOpen = true;
            }
        }
    }
    class NavigationViewManager{

        public void setNavHeader(){
            // 네비게이션 뷰 헤더부분 설정
            View navHeader=navigationView.getHeaderView(0);

            TextView name=navHeader.findViewById(R.id.name);
            TextView userid=navHeader.findViewById(R.id.userid);
            final TextView logout=navHeader.findViewById(R.id.logout);
            final TextView editprofile=navHeader.findViewById(R.id.editprofile);


            name.setText(userInfo.getName()+" 님");
            userid.setText(userInfo.getId());

            profile=navHeader.findViewById(R.id.imageView);

            //navigationView.addHeaderView(navHeader);

            logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    createLogoutDialog();
                }
            });
            logout.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if(motionEvent.getAction()==MotionEvent.ACTION_DOWN){
                        logout.setBackground(getResources().getDrawable(R.drawable.buttonlayout5));
                    }
                    else if(motionEvent.getAction()!=MotionEvent.ACTION_DOWN){
                        logout.setBackground(getResources().getDrawable(R.drawable.buttonlayout));
                    }
                    return false;
                }
            });
            editprofile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    requestPermissions();
                    createPhotoSelectDialog();
                }
            });
            editprofile.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {

                    if(motionEvent.getAction()==MotionEvent.ACTION_DOWN){
                        editprofile.setBackground(getResources().getDrawable(R.drawable.buttonlayout5));
                    }
                    else if(motionEvent.getAction()!=MotionEvent.ACTION_DOWN){
                        editprofile.setBackground(getResources().getDrawable(R.drawable.buttonlayout));
                    }
                    return false;
                }
            });
        }

    }

    @Override
    public void onListFragmentInteraction(BreakfastContent.DummyItem item) {

    }

    @Override
    public void onListFragmentInteraction(TempContent.DummyItem item) {

    }

    @Override
    public void onListFragmentInteraction(LunchContent.DummyItem item) {

    }

    @Override
    public void onListFragmentInteraction(DinnerContent.DummyItem item) {

    }
}

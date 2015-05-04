package com.jzlg.excellentwifi.activity;


import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.InfoWindow.OnInfoWindowClickListener;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.jzlg.excellentwifi.R;
import com.jzlg.excellentwifi.entity.WifiInfo;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class MapActivity extends BaseActivity {
	private Context context;
	private MapView mMapView;
	private BaiduMap mBaiduMap;
	private LocationMode mLocationMode;
	private LocationClient mLocationClient;
	private MyLocationListener mLocationListener;
	private BitmapDescriptor mIconLocation;
	private float mCurrentX;
	private double mLatitude;
	private double mLongitude;
	public boolean isFirstIn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// ��ʹ��SDK�����֮ǰ��ʼ��context��Ϣ������ApplicationContext
		// ע��÷���Ҫ��setContentView����֮ǰʵ��
		SDKInitializer.initialize(getApplicationContext());
		setContentView(R.layout.layout_map);
		this.context = this;
		// ��ʼ�����
		initView();
		// ��ʼ����λ
		initLocation();
		// ��ʼ��������
		initMarker();
		// ����¼�
		mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {

			@Override
			public boolean onMarkerClick(Marker marker) {

				Bundle bundle = marker.getExtraInfo();
				WifiInfo info = (WifiInfo) bundle.getSerializable("info");

				InfoWindow infoWindow;
				TextView tv = new TextView(context);
				tv.setBackgroundResource((R.drawable.map_tishi));
				tv.setPadding(30, 20, 30, 50);
				tv.setText(info.getName());
				tv.setTextColor(Color.parseColor("#ffffff"));
				BitmapDescriptor tvbit = BitmapDescriptorFactory.fromView(tv);
				final LatLng latLng = marker.getPosition();
				// Point point = mBaiduMap.getProjection()
				// .toScreenLocation(latLng);
				// point.y -= 50;// ����ƫ����
				// point.x -= 10;
				// LatLng ll =
				// mBaiduMap.getProjection().fromScreenLocation(point);
				infoWindow = new InfoWindow(tvbit, latLng, -50,
						new OnInfoWindowClickListener() {

							@Override
							public void onInfoWindowClick() {
								mBaiduMap.hideInfoWindow();
							}
						});
				mBaiduMap.showInfoWindow(infoWindow);

				return true;
			}
		});

		// �����ͼʱ��marker��ʧ
		mBaiduMap.setOnMapClickListener(new OnMapClickListener() {

			@Override
			public boolean onMapPoiClick(MapPoi mapPoi) {
				return false;
			}

			@Override
			public void onMapClick(LatLng latLng) {
				mBaiduMap.hideInfoWindow();
			}
		});
	}

	private void initMarker() {
	}

	// ��λ��ʼ��
	private void initLocation() {
		// Ĭ��ģʽ
		mLocationMode = LocationMode.NORMAL;

		mLocationClient = new LocationClient(this);
		// ��λ������
		mLocationListener = new MyLocationListener();
		// ע��
		mLocationClient.registerLocationListener(mLocationListener);
		// ����һЩ��Ҫ������
		setLocationOption();

		// ��ʼ������ͼ��
		mIconLocation = BitmapDescriptorFactory
				.fromResource(R.drawable.map_daohang);

		mLocationClient.start();
	}

	// ��λ����
	private void setLocationOption() {
		LocationClientOption option = new LocationClientOption();
		option.setCoorType("bd09ll");// ���صĶ�λ����ǰٶȾ�γ��,Ĭ��ֵgcj02
		option.setAddrType("all");// ���صĶ�λ���������ַ��Ϣ
		option.setIsNeedAddress(true);// λ�ã�һ��Ҫ���ã��������ò�����ַ
		option.setOpenGps(true);// ��GPS
		option.setScanSpan(1000);// �೤ʱ�����һ������
		// option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//
		// ��ȷ��λ
		mLocationClient.setLocOption(option);// ʹ������
	}

	private void initView() {
		// ��ȡ��ͼ�ؼ�����
		mMapView = (MapView) findViewById(R.id.map_bmapView);
		mBaiduMap = mMapView.getMap();

		// ���õ�ͼ�Ŵ���С����
		MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(15.0f);
		mBaiduMap.setMapStatus(msu);
	}

	// �˵�
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_map, menu);
		return true;
	}

	// �˵��������
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		// ��ͨ
		case R.id.map_common:
			mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
			break;
		// ����
		case R.id.map_site:
			mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
			break;
		// ʵʱ
		case R.id.map_traffic:
			// �Ƿ���ʾ��ʵʱ��ͼ
			if (mBaiduMap.isTrafficEnabled()) {
				mBaiduMap.setTrafficEnabled(false);
				item.setTitle("ʵʱ��ͨoff");
			} else {
				mBaiduMap.setTrafficEnabled(true);
				item.setTitle("ʵʱ��ͨon");
			}
			break;
		// �ҵ�λ��
		case R.id.map_location:
			centerToMyLocation();
			break;
		// ��ͨģʽ
		case R.id.map_sensormode_normal:
			mLocationMode = LocationMode.NORMAL;
			break;
		// ����ģʽ
		case R.id.map_sensormode_following:
			mLocationMode = LocationMode.FOLLOWING;
			break;
		// ����ģʽ
		case R.id.map_sensormode_compass:
			mLocationMode = LocationMode.COMPASS;
			break;
		// ������
		case R.id.map_add_overlay:
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	// ��λ���ҵ�λ��
	private void centerToMyLocation() {
		LatLng latLng = new LatLng(mLatitude, mLongitude);
		// ���þ�γ��
		MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
		mBaiduMap.animateMapStatus(msu);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mLocationClient.stop();
		mBaiduMap.setMyLocationEnabled(false);
		// ��activityִ��onDestroyʱִ��mMapView.onDestroy()��ʵ�ֵ�ͼ�������ڹ���
		mMapView.onDestroy();
		mMapView = null;
		
	}

	@Override
	protected void onResume() {
		super.onResume();
		// ��activityִ��onResumeʱִ��mMapView. onResume ()��ʵ�ֵ�ͼ�������ڹ���
		mMapView.onResume();
		
	}

	@Override
	protected void onStart() {
		super.onStart();
		// ������λ����
		mBaiduMap.setMyLocationEnabled(true);
		if (!mLocationClient.isStarted()) {
			mLocationClient.start();
		}

		
	}

	@Override
	protected void onStop() {
		super.onStop();
		// ֹͣ��λ
		mBaiduMap.setMyLocationEnabled(false);
		mLocationClient.stop();
		
	}

	@Override
	protected void onPause() {
		super.onPause();
		// ��activityִ��onPauseʱִ��mMapView. onPause ()��ʵ�ֵ�ͼ�������ڹ���
		mMapView.onPause();
		
	}

	private class MyLocationListener implements BDLocationListener {

		// ��λ�ɹ�֮��Ļص�
		@Override
		public void onReceiveLocation(BDLocation location) {
			MyLocationData data = new MyLocationData.Builder()// ����
					.direction(mCurrentX)//
					.accuracy(location.getRadius())//
					.latitude(location.getLatitude())//
					.longitude(location.getLongitude())//
					.build();

			// �����ҵĶ�λ
			mBaiduMap.setMyLocationData(data);

			// �����Զ���ͼ��
			MyLocationConfiguration config = new MyLocationConfiguration(
					mLocationMode, true, mIconLocation);

			mBaiduMap.setMyLocationConfigeration(config);

			// ȡ����γ��
			mLatitude = location.getLatitude();
			mLongitude = location.getLongitude();

			// �Ƿ��ǵ�һ�ζ�λ
			if (isFirstIn) {
				LatLng latLng = new LatLng(location.getLatitude(),
						location.getLongitude());

				// ���þ�γ��
				MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);

				mBaiduMap.animateMapStatus(msu);
				isFirstIn = false;

			}

			// Toast.makeText(
			// context,
			// "��λ�ɹ���γ��" + location.getLatitude() + "���ȣ�"
			// + location.getLongitude() + "��ַ�ǣ�"
			// + location.getAddrStr() + "�����ǣ�"
			// + location.getCity(), 0).show();

		}
	}

}

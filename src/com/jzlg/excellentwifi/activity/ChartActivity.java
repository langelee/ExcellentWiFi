package com.jzlg.excellentwifi.activity;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.listener.LineChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.LineChartView;

import com.jzlg.excellentwifi.R;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

/**
 * ͼ��
 * 
 * @author Administrator
 *
 */
public class ChartActivity extends Activity implements
		LineChartOnValueSelectListener {
	private LineChartView mChartView;
	private int numberOfLines = 1;// ��ǰ�ߵ�����
	private int maxNumberOfLines = 4;// ����ߵ�����
	private int numberOfPoints = 12;// ���������
	private float[][] randomNumbersTab = new float[maxNumberOfLines][numberOfPoints];

	private ValueShape shape = ValueShape.CIRCLE;// CIRCLE:Բ��;DIAMOND:����;SQUARE:������
	private boolean isCubic = false;// �Ƿ�������
	private boolean isFilled = false;// �Ƿ����
	private boolean hasLabels = true;// �Ƿ���ʾ��ֵ
	private boolean hasLabelsOnlyForSelected = false;// ������ѡ�еĵ������ʾ��ֵ
	private boolean hasLines = true;// ��
	private boolean hasPoints = true;// ��
	private LineChartData data;
	private boolean hasAxes = true;
	private boolean hasAxesNames = true;
	private ActionBar actionBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_chart);
		initView();
		initData();
		initEvent();
	}

	private void initEvent() {
		mChartView.setOnValueTouchListener(this);
	}

	private void initData() {
		generateValues();
		generateData();
	}

	private void initView() {
		actionBar = getActionBar();
		actionBar.setTitle("�ź�����");
		actionBar.setLogo(R.drawable.left_menu_levels_white);
		actionBar.setDisplayHomeAsUpEnabled(true);// ��������ͼ��
		mChartView = (LineChartView) findViewById(R.id.chart_linechart);
	}

	private void generateData() {
		List<Line> lines = new ArrayList<Line>();
		// numberOfLines=1;numberOfPoints=12
		for (int i = 0; i < numberOfLines; i++) {
			// ����Դ
			List<PointValue> values = new ArrayList<PointValue>();
			for (int j = 0; j < numberOfPoints; j++) {
				values.add(new PointValue(j, randomNumbersTab[i][j]));
			}
			// ������
			Line line = new Line(values);
			// �����е���ɫ
			line.setColor(ChartUtils.COLORS[i]);
			// ��״
			line.setShape(shape);
			// �Ƿ�������
			line.setCubic(isCubic);
			// ���
			line.setFilled(isFilled);
			// ��ǩ
			line.setHasLabels(hasLabels);
			// ������ѡ�е����ñ�ǩ
			line.setHasLabelsOnlyForSelected(hasLabelsOnlyForSelected);
			// ����
			line.setHasLines(hasLines);
			// ��
			line.setHasPoints(hasPoints);
			lines.add(line);
		}

		data = new LineChartData();
		data.setLines(lines);

		if (hasAxes) {
			Axis axisX = new Axis();
			Axis axisY = new Axis().setHasLines(true);
			if (hasAxesNames) {
				axisX.setName("ʱ��(s)");
				axisY.setName("�ź�ǿ��");
			}
			data.setAxisXBottom(axisX);
			data.setAxisYLeft(axisY);
		} else {
			data.setAxisXBottom(null);
			data.setAxisYLeft(null);
		}

		data.setBaseValue(Float.NEGATIVE_INFINITY);
		mChartView.setLineChartData(data);
	}

	// ��ȡ��������洢��4��12�еĶ�ά������
	private void generateValues() {
		// maxNumberOfLines=4;numberOfPoints=12
		for (int i = 0; i < numberOfLines; ++i) {
			for (int j = 0; j < numberOfPoints; ++j) {
				randomNumbersTab[i][j] = (float) Math.random() * 100f;
			}
		}
	}

	// �����¼�
	@Override
	public void onValueDeselected() {
	}

	@Override
	public void onValueSelected(int lineIndex, int pointIndex, PointValue value) {
		Toast.makeText(
				ChartActivity.this,
				"ֵSelected: " + value + "��lineIndex:" + lineIndex
						+ "��pointIndex:" + pointIndex, Toast.LENGTH_SHORT)
				.show();
	}

	// �˵�ѡ���¼�
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

}

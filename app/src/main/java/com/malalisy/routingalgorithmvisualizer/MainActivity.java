package com.malalisy.routingalgorithmvisualizer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.malalisy.routingalgorithmvisualizer.graph.Graph;
import com.malalisy.routingalgorithmvisualizer.graph.RoutingAlgorithm;
import com.malalisy.routingalgorithmvisualizer.graph.Vertex;
import com.malalisy.routingalgorithmvisualizer.visualization.VisualizationView;
import com.shawnlin.numberpicker.NumberPicker;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    Graph graph;

    VisualizationView visualizationView;


    Button btnDraw, btnFindMaxBW, btnShowPath;

    NumberPicker verticesNumberPicker, sourceNumber, destinationNumber;

    LinearLayout resultContainer, destinationContainer;


    int currentSource;


    Animation animIn, animOut;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


        visualizationView = (VisualizationView) findViewById(R.id.visualizationView);

        btnDraw = (Button) findViewById(R.id.btnSet);
        btnDraw.setOnClickListener(this);

        btnFindMaxBW = (Button) findViewById(R.id.btnFindMaxBW);
        btnFindMaxBW.setOnClickListener(this);

        btnShowPath = (Button) findViewById(R.id.btnShowPath);
        btnShowPath.setOnClickListener(this);

        verticesNumberPicker = (NumberPicker) findViewById(R.id.nOfVertices);
        verticesNumberPicker.setMaxValue(AppConstants.MAX_NUMBER_OF_VERTICES);
        verticesNumberPicker.setMinValue(AppConstants.MIN_NUMBER_OF_VERTICES);
        verticesNumberPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        verticesNumberPicker.setValue(AppConstants.DEFAULT_NUMBER_OF_VERTICES);


        sourceNumber = (NumberPicker) findViewById(R.id.sourceNumber);
        sourceNumber.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        sourceNumber.setMaxValue(AppConstants.DEFAULT_NUMBER_OF_VERTICES);
        sourceNumber.setMinValue(1);

        destinationNumber = (NumberPicker) findViewById(R.id.destinationNumber);
        destinationNumber.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        destinationNumber.setMinValue(1);
        destinationNumber.setMaxValue(AppConstants.DEFAULT_NUMBER_OF_VERTICES);


        resultContainer = (LinearLayout) findViewById(R.id.resultContainer);

        destinationContainer = (LinearLayout) findViewById(R.id.destinationContainer);


        graph = Graph.generateRandomly(AppConstants.DEFAULT_NUMBER_OF_VERTICES);
        visualizationView.updateGraph(graph);

        animOut = AnimationUtils.loadAnimation(this, R.anim.scale_fade_out);
        animOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                destinationContainer.setVisibility(View.INVISIBLE);
                resultContainer.setVisibility(View.INVISIBLE);
                displayResult();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        animIn = AnimationUtils.loadAnimation(this, R.anim.scale_fade_in);
        animIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                resultContainer.setVisibility(View.VISIBLE);
                destinationContainer.setVisibility(View.VISIBLE);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();

        visualizationView.onResume();

    }


    @Override
    protected void onPause() {
        super.onPause();

        visualizationView.onPause();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btnSet:

                currentSource = -1;
                int nOfVertices = verticesNumberPicker.getValue();

                graph = Graph.generateRandomly(nOfVertices);
                visualizationView.updateGraph(graph);

                sourceNumber.setMaxValue(nOfVertices);
                sourceNumber.setValue(1);

                destinationNumber.setMaxValue(nOfVertices);
                destinationNumber.setValue(1);

                if (resultContainer.getVisibility() == View.VISIBLE) {

                    Animation out = AnimationUtils.loadAnimation(this, R.anim.scale_fade_out);
                    out.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            destinationContainer.setVisibility(View.INVISIBLE);
                            resultContainer.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    resultContainer.startAnimation(out);

                    resultContainer.removeAllViews();
                    destinationContainer.startAnimation(out);
                }


                break;

            case R.id.btnFindMaxBW:

                if (currentSource == sourceNumber.getValue())
                    return;
                RoutingAlgorithm routingAlgorithm = new RoutingAlgorithm(graph);

                routingAlgorithm.calculateMaxBW(sourceNumber.getValue());
                currentSource = sourceNumber.getValue();

                if (resultContainer.getVisibility() == View.VISIBLE) {

                    resultContainer.startAnimation(animOut);
                    destinationContainer.startAnimation(animOut);

                } else
                    displayResult();

                break;


            case R.id.btnShowPath:

                if (destinationNumber.getValue() == currentSource) {

                    Toast.makeText(this, "Source and destination can not be same!", Toast.LENGTH_SHORT).show();
                    return;
                }

                visualizationView.drawPathTo(destinationNumber.getValue());

                break;


        }
    }

    private void displayResult() {


        resultContainer.removeAllViews();

        Vertex[] vertices = graph.getVertices();
        TextView tv;

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        tv = new TextView(MainActivity.this);
        tv.setLayoutParams(params);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);

        String source = "Source : " + sourceNumber.getValue();
        tv.setText(source);
        tv.setTextColor(0xFF880E4F);
        tv.setPadding(8, 16, 8, 16);

        resultContainer.addView(tv);


        for (int i = 0; i < graph.getNumberOfVertices(); i++) {

            if (graph.vertixOfIndex(i) == sourceNumber.getValue())
                continue;

            tv = new TextView(MainActivity.this);
            tv.setLayoutParams(params);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            tv.setPadding(8, 8, 8, 8);

            String s = "Max Bandwidth To " + vertices[i].getName() + " : " + (int) vertices[i].getValue();
            tv.setText(s);


            tv.setTextColor(0xFF00C853);

            resultContainer.addView(tv);


        }


        if (resultContainer.getVisibility() != View.VISIBLE) {
            resultContainer.startAnimation(animIn);
            destinationContainer.startAnimation(animIn);
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.about:

                Intent intent = new Intent(this, About.class);
                startActivity(intent);

                break;


        }

        return super.onOptionsItemSelected(item);
    }
}

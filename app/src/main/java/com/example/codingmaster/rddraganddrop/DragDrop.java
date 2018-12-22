package com.example.codingmaster.rddraganddrop;

import android.content.ClipData;
import android.content.ClipDescription;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class DragDrop extends AppCompatActivity implements View.OnDragListener, View.OnLongClickListener {

    private ImageView ball_1, ball_2;
    private static final String BALL_FIRST_TAG = "FOOT BALL";
    private static final String BALL_SECOND_TAG = "CRICKET BALL";
    View previousView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drag_drop);
        findViews();
        setTag();
        implementEvents();
    }

    //Find all views and set Tag to all draggable views
    private void findViews() {
        ball_1 = findViewById(R.id.ball_1);
        ball_2 = findViewById(R.id.ball_2);

    }

    //set tag in views
    private void setTag() {
        ball_2.setTag(BALL_SECOND_TAG);
        ball_1.setTag(BALL_FIRST_TAG);
    }

    //Implement long click and drag listener
    private void implementEvents() {
        //add or remove any view that you don't want to be dragged
        ball_1.setOnLongClickListener(this);
        ball_2.setOnLongClickListener(this);

        //add or remove any layout view that you don't want to accept dragged view
        findViewById(R.id.top_layout).setOnDragListener(this);
        findViewById(R.id.right_layout).setOnDragListener(this);
    }

    @Override
    public boolean onLongClick(View view) {
        // Create a new ClipData.
        ClipData.Item item = new ClipData.Item((CharSequence) view.getTag());
        // ClipData, and set its MIME type entry to "text/plain"
        String[] mimeTypes = {ClipDescription.MIMETYPE_TEXT_PLAIN};
        ClipData data = new ClipData(view.getTag().toString(), mimeTypes, item);

        // Instantiates the drag shadow builder.
        View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
        // Starts the drag
        view.startDrag(data//data to be dragged
                , shadowBuilder //drag shadow
                , view//local data about the drag and drop operation
                , 0//no needed flags
        );

        //view assign
        previousView = view;
        view.setVisibility(View.INVISIBLE);
        return true;
    }

    @Override
    public boolean onDrag(View view, DragEvent event) {
        // Defines a variable to store the action type for the incoming event
        int action = event.getAction();
        switch (action) {
            case DragEvent.ACTION_DRAG_STARTED:
                // Determines if this View can accept the dragged data
                if (event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                    // returns true to indicate that the View can accept the dragged data.
                    return true;
                }
                // Returns false. During the current drag and drop operation, this View will
                // not receive events again until ACTION_DRAG_ENDED is sent.
                return false;

            case DragEvent.ACTION_DRAG_ENTERED:
                // Return true; the return value is ignored.
                return true;
            case DragEvent.ACTION_DRAG_LOCATION:
                // Ignore the event
                return true;
            case DragEvent.ACTION_DRAG_EXITED:

                return true;

            case DragEvent.ACTION_DROP:
                // Gets the item containing the dragged data
                ClipData.Item item = event.getClipData().getItemAt(0);
                // Gets the text data from the item.
                String dragData = item.getText().toString();
                // Displays a message containing the dragged data.
                Toast.makeText(this, "Ball Type : " + dragData, Toast.LENGTH_SHORT).show();
                View v = (View) event.getLocalState();
                ViewGroup owner = (ViewGroup) v.getParent();

                //remove the dragged view
                LinearLayout container = (LinearLayout) view;
                if (container.isEnabled()) {
                    owner.removeView(v);
                } else {
                    LinearLayout container1 = (LinearLayout) owner;
                    container1.addView(v);
                }
                //caste the view into LinearLayout as our drag acceptable layout is LinearLayout
                container.addView(v);
                //Add the dragged view
                v.setVisibility(View.VISIBLE);//finally set Visibility to VISIBLE

                // Returns true. DragEvent.getResult() will return true.
                return true;
            case DragEvent.ACTION_DRAG_ENDED:

                // Does a getResult(), and displays what happened.
                if (event.getResult()) {

                } else
                    previousView.setVisibility(View.VISIBLE);

                // returns true; the value is ignored.
                return true;

// An unknown action type was received.
            default:
                break;
        }
        return false;
    }


}
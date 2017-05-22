package hu.ait.jam.touch;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.google.firebase.auth.FirebaseAuth;

import hu.ait.jam.R;

public class ItemTouchHelperCallback extends
        ItemTouchHelper.Callback {

    private TouchHelperAdapter touchHelperAdapter;
    private Context context;

    public ItemTouchHelperCallback(TouchHelperAdapter todoTouchHelperAdapter, Context context) {
        this.touchHelperAdapter = todoTouchHelperAdapter;
        this.context = context;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        int swipeFlags = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                          RecyclerView.ViewHolder target) {
        touchHelperAdapter.onItemMove(viewHolder.getAdapterPosition(),
                target.getAdapterPosition()
        );
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        if (direction == ItemTouchHelper.RIGHT) {
            int position = viewHolder.getAdapterPosition();
            determineMatch(position);
        }

        touchHelperAdapter.onItemDismiss(viewHolder.getAdapterPosition());
    }


    public void determineMatch(int position) {
        String currentEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        String otherEmail = touchHelperAdapter.getOtherEmail(position);
        boolean isMatch = touchHelperAdapter.checkMatch(currentEmail, otherEmail);

        if (isMatch) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(R.string.match_success);
            builder.setNeutralButton(R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            builder.show();
        }
    }
}
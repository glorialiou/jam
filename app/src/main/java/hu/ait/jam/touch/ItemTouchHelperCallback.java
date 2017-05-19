package hu.ait.jam.touch;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.google.firebase.auth.FirebaseAuth;

public class ItemTouchHelperCallback extends
        ItemTouchHelper.Callback {

    private TouchHelperAdapter touchHelperAdapter;

    public ItemTouchHelperCallback(TouchHelperAdapter todoTouchHelperAdapter, Context context) {
        this.touchHelperAdapter = todoTouchHelperAdapter;
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
        } else {
            touchHelperAdapter.onItemDismiss(viewHolder.getAdapterPosition());
        }
    }


    public void determineMatch(int position) {
        String currentEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        String otherEmail = touchHelperAdapter.getOtherEmail(position);
        String otherName = touchHelperAdapter.getOtherName(position);
        String otherPhone = touchHelperAdapter.getOtherPhone(position);
        touchHelperAdapter.checkMatch(currentEmail, otherEmail, otherName, otherPhone);
    }
}
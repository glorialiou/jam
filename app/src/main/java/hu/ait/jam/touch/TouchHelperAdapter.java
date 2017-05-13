package hu.ait.jam.touch;

public interface TouchHelperAdapter {
    void onItemDismiss(int position);
    void onItemMove(int fromPosition, int toPosition);
}

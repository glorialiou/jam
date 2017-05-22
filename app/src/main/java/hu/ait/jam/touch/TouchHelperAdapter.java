package hu.ait.jam.touch;

public interface TouchHelperAdapter {
    String getOtherEmail(int adapterPosition);
    boolean checkMatch(String currentEmail, String otherEmail);
    void onItemDismiss(int position);
    void onItemMove(int fromPosition, int toPosition);
}

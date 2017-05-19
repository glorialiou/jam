package hu.ait.jam.touch;

public interface TouchHelperAdapter {
    String getOtherEmail(int adapterPosition);
    String getOtherName(int adapterPosition);
    String getOtherPhone(int adapterPosition);
    void checkMatch(String currentEmail, String otherEmail, String otherName, String otherPhone);
    void onItemDismiss(int position);
    void onItemMove(int fromPosition, int toPosition);
}

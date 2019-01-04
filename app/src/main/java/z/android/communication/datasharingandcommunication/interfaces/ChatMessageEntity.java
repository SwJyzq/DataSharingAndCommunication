package z.android.communication.datasharingandcommunication.interfaces;

public class ChatMessageEntity {
    private String name,date,message;
    private int image;
    private boolean isComeMessage;

    public ChatMessageEntity(){

    }

    public ChatMessageEntity(String name,String date,String text,int image,boolean isComeMessage){
        super();
        this.date = date;
        this.name = name;
        this.message = text;
        this.image = image;
        this.isComeMessage = isComeMessage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setMessageType(boolean isComeMessage){
        this.isComeMessage = isComeMessage;
    }

    public boolean getMessageType(){
        return this.isComeMessage;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public int getImage() {
        return image;
    }
}

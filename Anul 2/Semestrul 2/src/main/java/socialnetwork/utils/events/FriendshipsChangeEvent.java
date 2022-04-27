package socialnetwork.utils.events;

import socialnetwork.domain.Prietenie;
import socialnetwork.domain.UserFriendshipDTO;

public class FriendshipsChangeEvent implements Event{
    private ChangeEventType type;
    private Prietenie data, oldData;

    public FriendshipsChangeEvent(ChangeEventType type, Prietenie data, Prietenie oldData) {
        this.type = type;
        this.data = data;
        this.oldData = oldData;
    }

    public FriendshipsChangeEvent(ChangeEventType type, Prietenie data) {
        this.type = type;
        this.data = data;
    }

    public Prietenie getData() {
        return data;
    }

    public ChangeEventType getType() {
        return type;
    }

    public Prietenie getOldData() {
        return oldData;
    }

}

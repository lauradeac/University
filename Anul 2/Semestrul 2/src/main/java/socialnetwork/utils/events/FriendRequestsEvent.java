package socialnetwork.utils.events;

import socialnetwork.domain.FriendRequest;
import socialnetwork.domain.Prietenie;

public class FriendRequestsEvent implements Event{
    private ChangeEventType type;
    private FriendRequest data, oldData;

    public FriendRequestsEvent(ChangeEventType type, FriendRequest data) {
        this.type = type;
        this.data = data;
    }

    public FriendRequestsEvent(ChangeEventType type, FriendRequest data, FriendRequest oldData) {
        this.type = type;
        this.data = data;
        this.oldData = oldData;
    }

    public ChangeEventType getType() {
        return type;
    }

    public FriendRequest getData() {
        return data;
    }

    public FriendRequest getOldData() {
        return oldData;
    }
}


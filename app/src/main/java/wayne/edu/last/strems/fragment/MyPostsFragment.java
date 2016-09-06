package wayne.edu.last.strems.fragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

public class MyPostsFragment extends PostListFragment {

    public MyPostsFragment() {}

    @Override
    public Query getQuery(DatabaseReference databaseReference) {
        // All my posts
      //  return databaseReference.child("user-emergencies")
          //      .child(getUid());
        return databaseReference.child("emergencies").orderByChild("uid").equalTo(getUid());
    }
}

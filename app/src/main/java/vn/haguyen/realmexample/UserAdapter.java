package vn.haguyen.realmexample;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

public class UserAdapter extends RealmRecyclerViewAdapter<User, UserAdapter.ViewHolder> {
    private Context context;
    OrderedRealmCollection<User> data;

    public UserAdapter( OrderedRealmCollection<User> data, Context c) {
        super(data, true);
        context = c;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemline,parent,false);


        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        User obj = getItem(position);
        holder.user = obj;
        Picasso.with(context).load(obj.getAvatar_url()).into(holder.tvUserAvatar);
        holder.tvLoginName.setText(obj.getLogin());
        holder.tvUserType.setText(obj.getType());

    }
    public void removeItem(int postion) {
       data.remove(postion);
        notifyItemRemoved(postion);
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvLoginName;
        public TextView tvUserType;
        public ImageView tvUserAvatar;
        public User user;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvLoginName = (TextView) itemView.findViewById(R.id.user_name);
            tvUserType =(TextView) itemView.findViewById(R.id.user_type);
            tvUserAvatar =(ImageView) itemView.findViewById(R.id.user_avatar);

        }
    }
}

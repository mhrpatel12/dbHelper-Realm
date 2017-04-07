
import android.content.Context;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import io.realm.exceptions.RealmException;

/**
 * Created by patelmih on 6/29/2016.
 */
public class LoadBLL {

    private Context mContext;

    public LoadBLL(Context con) {
        this.mContext = con;
    }

    public void insertLoads(final ArrayList<Load> loads) {

        Realm realm = Realm.getInstance(YourApplication.getRealmConfig());

        try {
            realm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    for (int i = 0; i < loads.size(); i++) {
                        realm.copyToRealmOrUpdate(loads.get(i));
                    }
                }
            });

        } catch (RealmException e) {
            realm.cancelTransaction();
            LogUtils.LOGE(getClass().getSimpleName(),e);
        } finally {
            realm.close();
        }

    }

    public ArrayList<Load> getLoads(int status) {
        String s = null;
        Realm realm = Realm.getInstance(YourApplication.getRealmConfig());
        RealmResults<Load> loadData;
        if (status == 0) {
            loadData = realm.where(Load.class).findAllSorted("postedOn", Sort.DESCENDING);
        } else {
            loadData = realm.where(Load.class)
                    .equalTo("statusId", status)
                    .findAllSorted("postedOn", Sort.DESCENDING);
        }

        ArrayList<Load> arrContactsBids = new ArrayList(loadData);

        realm.close();

        return arrContactsBids;
    }

    public Load getLoadData(long id) {

        Realm realm = Realm.getInstance(YourApplication.getRealmConfig());
        realm.beginTransaction();
        Load loadData = realm.where(Load.class).equalTo("id", id).findFirst();
        realm.commitTransaction();
        realm.close();
        return loadData;
    }

}

package lima.rocha.contadordefaltas;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class AdapterListView extends ArrayAdapter<ItemListLite> {
    final private String TAG = "ADAPTER";

    private HashMap<String, Integer> reverseList;
    /**
     * Constructor
     *
     * @param context  The current context.
     * @param resource The resource ID for a layout file containing a TextView to use when
     *                 instantiating views.
     * @param objects  The objects to represent in the ListView.
     */
    public AdapterListView(Context context, int resource, ArrayList<ItemListLite> objects) {
        super(context, resource, objects);
        reverseList = new HashMap<>();
        for (ItemListLite item: objects){
            reverseList.put(item.getMateria(), objects.indexOf(item));
        }

    }

    public void add(ItemListLite ill){
        super.add(ill);
        reverseList.put(ill.getMateria(), super.getPosition(ill));
    }

    public ItemListLite getItem(String materia){
        ItemListLite i = null;
        try {
            i = super.getItem(reverseList.get(materia));
        } catch (Exception e){
            Log.e(TAG, Log.getStackTraceString(e));
        }
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        Log.v(TAG, "Updating view");
        if (convertView == null){
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_list, null);
        }
        ItemListLite p = getItem(position);
        if (p != null){
            Log.v(TAG, "p != null");
            Button dele = (Button) convertView.findViewById(R.id.Delete);
            TextView perc = (TextView) convertView.findViewById(R.id.Porcentagem);
            TextView dist = (TextView) convertView.findViewById(R.id.Distribuicao);
            TextView mate = (TextView) convertView.findViewById(R.id.Materia);

            if (perc != null){
                String percent = p.getPercentagem();
                perc.setText(percent);
                int colSel = Integer.parseInt(percent.substring(0,percent.length()-1));
                if (colSel < 12)
                    perc.setTextColor(getContext().getResources().getColor(R.color.lightGreen));
                else if (colSel < 18)
                    perc.setTextColor(getContext().getResources().getColor(R.color.lightYellow));
                else
                    perc.setTextColor(getContext().getResources().getColor(R.color.lightRed));

            }

            if (dist != null){
                dist.setText(p.getDistribuicao());
            }

            if (mate != null){
                mate.setText(p.getMateria());
                mate.setTag(position);
            }

            if (dele != null){
                dele.setTag(position);
                Log.v(TAG, Integer.toString(position));
            }

        }
        return convertView;
    }
}
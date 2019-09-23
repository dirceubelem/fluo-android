package br.com.fluo.fluo.database.fw;

import android.content.Context;
import android.util.Log;

import java.util.List;

public class BOFactory {

    public static TOBase get(Context c, TOBase to) {
        try {
            return DAOBase.get(c, to);
        } catch (Exception e) {
            Log.e("App", e.getMessage());
            return null;
        }
    }

    public static void insert(Context c, TOBase to) {
        try {
            DAOBase.insert(c, to);
        } catch (Exception e) {
            Log.e("App", e.getMessage());
        }
    }


    public static void update(Context c, TOBase to) {
        try {
            DAOBase.update(c, to);
        } catch (Exception e) {
            Log.e("App", e.getMessage());
        }
    }

    public static void deleteAll(Context c, TOBase to) {
        try {
            DAOBase.deleteAll(c, to);
        } catch (Exception e) {
            Log.e("App", e.getMessage());
        }
    }

    public static List<TOBase> list(Context c, TOBase to, String order) {
        try {
            return DAOBase.list(c, to, order);
        } catch (Exception e) {
            Log.e("App", e.getMessage());
            return null;
        }
    }

    public static void delete(Context c, TOBase to) {
        try {
            DAOBase.delete(c, to);
        } catch (Exception e) {
            Log.e("App", e.getMessage());
        }
    }


}
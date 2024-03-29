package com.tfg.jonay.videovigilancia;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * Created by jonay on 16/02/16.
 */
public class BaseDeDatos {
    private SQLiteDatabase db;

    public BaseDeDatos(SQLiteDatabase bdd){
        db = bdd;
    }

    public void crearTablas(){
        db.execSQL("CREATE TABLE IF NOT EXISTS destinatarios(nombre VARCHAR, numero VARCHAR, activo BOOLEAN);");
        db.execSQL("CREATE TABLE IF NOT EXISTS servidor(url VARCHAR, user VARCHAR, pass VARCHAR, web VARCHAR, nombrecam VARCHAR);");
        db.execSQL("CREATE TABLE IF NOT EXISTS robot(nombre VARCHAR);");
    }


    /* Tabla destinatarios */

    public void addDestinatario(String nombre, String numero){
        db.execSQL("INSERT INTO destinatarios VALUES('" + nombre + "', '" + numero +"', 1);");
    }

    public void addDestinatario(String numero){
        db.execSQL("INSERT INTO destinatarios VALUES('" + "Sin nombre" + "', '" + numero +"', 1);");
    }

    public void delDestinatario(String numero){
        db.execSQL("DELETE FROM destinatarios WHERE numero = " + numero + ";");
    }

    public void updateDestinatarioNombre(String numero, String nombre_nuevo){
        db.execSQL("UPDATE destinatarios SET nombre = '" + nombre_nuevo + "' WHERE numero = " + numero + ";");
    }

    public void updateDestinatarioEstado(String numero, int estado){
        db.execSQL("UPDATE destinatarios SET activo = " + Integer.toString(estado) + " WHERE numero = " + numero + ";");
    }

    public ArrayList<Contacto> selectDestinatarios(){
        ArrayList<Contacto> lista_destinatarios = new ArrayList<>();
        Cursor result = db.rawQuery("SELECT * FROM destinatarios;", null);
        while(result.moveToNext()){
            boolean est;
            if(result.getInt(2) == 0){
                est = false;
            }else{
                est = true;
            }
            lista_destinatarios.add(new Contacto(result.getString(0), result.getString(1), est));
        }

        return lista_destinatarios;
    }


    /* Tabla servidor */

    public void updateServidor(String url, String user, String pass, String web, String nombrecam){
        db.execSQL("DELETE FROM servidor");
//        db.execSQL("INSERT INTO servidor VALUES('" + url + "', '" + user + "', '" + pass + "');");
        db.execSQL("INSERT INTO servidor VALUES('" + url + "', '" + user + "', '" + pass + "', '" + web + "', '" + nombrecam + "');");
    }

    public String[] getServData(){
        String[] datos = new String[5];
        Cursor result = db.rawQuery("SELECT * FROM servidor;", null);
        while(result.moveToNext()){
            datos[0] = result.getString(0);
            datos[1] = result.getString(1);
            datos[2] = result.getString(2);
            datos[3] = result.getString(3);
            datos[4] = result.getString(4);
        }
        return datos;
    }

    /* Tabla robot */

    public void updateRobot(String nombre){
        db.execSQL("DELETE FROM robot");
        db.execSQL("INSERT INTO robot VALUES('" + nombre + "')");
    }

    public String getRobotData(){
        String datos = null;
        Cursor result = db.rawQuery("SELECT * FROM robot;", null);
        while(result.moveToNext()){
            datos = result.getString(0);
        }
        return datos;
    }
}

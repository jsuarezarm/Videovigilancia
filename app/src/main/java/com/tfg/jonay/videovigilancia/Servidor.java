package com.tfg.jonay.videovigilancia;

import android.app.Activity;
import android.content.Context;
import android.view.SurfaceHolder;

import net.majorkernelpanic.streaming.Session;
import net.majorkernelpanic.streaming.SessionBuilder;
import net.majorkernelpanic.streaming.audio.AudioQuality;
import net.majorkernelpanic.streaming.gl.SurfaceView;
import net.majorkernelpanic.streaming.rtsp.RtspClient;
import net.majorkernelpanic.streaming.video.VideoQuality;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by jonay on 29/02/16.
 */
public class Servidor extends Activity implements RtspClient.Callback,
        Session.Callback, SurfaceHolder.Callback{

    private String stream_url;
    private String publisher_user;
    private String publisher_pass;

    private RtspClient client;
    private Session session;

    SurfaceView surfaceView;

    public Servidor(){

    }

    public void cargarDesdeBDD(String[] datos){
        stream_url = datos[0];
        publisher_user = datos[1];
        publisher_pass = datos[2];
    }

    public void iniciar(SurfaceView sView, Context context){
        System.out.println(stream_url);
        System.out.println(publisher_user);
        System.out.println(publisher_pass);

        surfaceView = sView;

        session = SessionBuilder.getInstance()
                .setCallback(this)
                .setSurfaceView(surfaceView)
                .setPreviewOrientation(90)
                .setContext(context)
                .setAudioEncoder(SessionBuilder.AUDIO_AAC)
                .setAudioQuality(new AudioQuality(8000, 16000))
                .setVideoEncoder(SessionBuilder.VIDEO_H264)
//                .setVideoQuality(new VideoQuality(320,240,20,500000))
                .setVideoQuality(new VideoQuality(1280, 720, 120, 500000))
                .build();

        surfaceView.getHolder().addCallback(this);

        client = new RtspClient();
        client.setSession(session);
        client.setCallback(this);
        surfaceView.setAspectRatioMode(SurfaceView.ASPECT_RATIO_PREVIEW);

        Pattern uri = Pattern.compile("rtsp://(.+):(\\d+)/(.+)");
        Matcher m = uri.matcher(stream_url);
        m.find();
        String ip, port, path;
        ip = m.group(1);
        port = m.group(2);
        path = m.group(3);

        System.out.println(ip);
        System.out.println(port);
        System.out.println(path);


        client.setCredentials(publisher_user, publisher_pass);
        client.setServerAddress(ip, Integer.parseInt(port));
        client.setStreamPath("/" + path);
    }

    public void iniciarStreaming(){
        if(!client.isStreaming()){
            System.out.println("iniciar");
            session.startPreview();
            client.startStream();
        }else{
            session.stopPreview();
            client.stopStream();
        }
    }

    public void iniciarFlash(){
        session.toggleFlash();
    }

    public void parar(){
        session.release();
        client.release();
        surfaceView.getHolder().removeCallback(this);
    }

    public Session getSession(){
        return session;
    }

    public String getURL(){
        return stream_url;
    }

    public void setURL(String url){
        stream_url = url;
    }

    public String getUsername(){
        return publisher_user;
    }

    public void setUsername(String user){
        publisher_user = user;
    }

    public String getPassword(){
        return publisher_pass;
    }

    public void setPassword(String pass){
        publisher_pass = pass;
    }

    @Override
    public void onBitrateUpdate(long bitrate) {

    }

    @Override
    public void onSessionError(int reason, int streamType, Exception e) {

    }

    @Override
    public void onPreviewStarted() {
        System.out.println("start preview");
    }

    @Override
    public void onSessionConfigured() {

    }

    @Override
    public void onSessionStarted() {
        System.out.println("start session");
    }

    @Override
    public void onSessionStopped() {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        System.out.println("surface creado");
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    public void onRtspUpdate(int message, Exception exception) {

    }
}
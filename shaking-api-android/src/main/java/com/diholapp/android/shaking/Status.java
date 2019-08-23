package com.diholapp.android.shaking;

class Status {

    private boolean stopped = true;
    private boolean paused = false;
    private boolean processing = false;

    public boolean start(){

        if(stopped){
            paused = false;
            stopped = false;
            processing = false;
            return true;
        }
        return false;
    }

    public boolean stop(){

        if(!stopped){
            paused = false;
            stopped = true;
            processing = false;
            return true;
        }
        return false;
    }

    public boolean restart(){
        if(!stopped && !processing && paused){
            paused = false;
            return true;
        }
        return false;
    }

    public boolean pause(){
        if(!stopped){
            paused = true;
            return true;
        }
        return false;
    }

    public boolean isStopped() {
        return stopped;
    }

    public void setStopped(boolean stopped) {
        this.stopped = stopped;
    }

    public boolean isPaused() {
        return paused;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    public boolean isProcessing() {
        return processing;
    }

    public void setProcessing(boolean processing) {
        this.processing = processing;
    }

}

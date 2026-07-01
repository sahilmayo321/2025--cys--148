public class Task5_Smartphone {

    interface Camera {
        void clickPhoto();
        void clickVideo();
    }

    interface MediaPlayer {
        void playVideo();
        void playAudio();
    }

    interface Gps {
        void location();
        void destination();
    }

    abstract static class Mobile {
        abstract void mobileOn();
        abstract void mobileOff();
    }

    static class Smartphone extends Mobile implements Camera, MediaPlayer, Gps {
        @Override
        void mobileOn() {
            System.out.println("Mobile is on");
        }

        @Override
        void mobileOff() {
            System.out.println("Mobile is off");
        }

        @Override
        public void clickPhoto() {
            System.out.println("Click a photo");
        }

        @Override
        public void clickVideo() {
            System.out.println("Click a video");
        }

        @Override
        public void location() {
            System.out.println("Location is on");
        }

        @Override
        public void destination() {
            System.out.println("Destination is found");
        }

        @Override
        public void playVideo() {
            System.out.println("Video is playing");
        }

        @Override
        public void playAudio() {
            System.out.println("Audio is playing");
        }
    }

    public static void main(String[] args) {
        Smartphone sp = new Smartphone();
        sp.mobileOn();
        sp.clickPhoto();
        sp.clickVideo();
        sp.playVideo();
        sp.playAudio();
        sp.location();
        sp.destination();
        sp.mobileOff();
    }
}

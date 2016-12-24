package blue.made.angleshared.asset;

/**
 * Created by Sam Sartor on 12/23/2016.
 */
public interface WaitingAssetSource<A> extends AssetSource<A> {
    public boolean isReady();

    public void setReady();
}

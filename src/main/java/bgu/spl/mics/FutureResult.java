package bgu.spl.mics;

public class FutureResult<T,A> {
    /**
     * represents if a gadget/agent available
     */
    private boolean isAvailable;
    /**
     * two fields that represents more of the future result:
     * for Moneypenny: its serial number and List for agents names
     * for Q: used for handle the time it got its event
     */
    private T secResult;
    private A thirdResult;

    public FutureResult(boolean isAvailable,T secResult,A thirdResult){
        this.isAvailable=isAvailable;
        this.secResult=secResult;
        this.thirdResult=thirdResult;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public T getSecResult() {
        return secResult;
    }

    public A getThirdResult() {
        return thirdResult;
    }
}

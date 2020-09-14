package ir.aminer.potadoshack.client.page.callbacks;

import ir.aminer.potadoshack.core.error.Error;

import java.io.IOException;

public interface IPageError {
    void onError(Error error) throws IOException;
}

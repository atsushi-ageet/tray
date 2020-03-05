package net.grandcentrix.tray.provider;

import net.grandcentrix.tray.core.TrayRuntimeException;

import android.net.Uri;

/**
 * Created by pascalwelsch on 4/12/15.
 */
public class TrayContractTest extends TrayProviderTestCase {


    public void testConstruction() throws Exception {
        new TrayContract();
    }

    public void testGenerateContentUri() throws Exception {
        Uri uri = TrayContract.generateContentUri(getContext());
        assertEquals("content://net.grandcentrix.tray.test/preferences",
                uri.toString());

        TrayContract.sAuthority = "asdf";
        uri = TrayContract.generateContentUri(getContext());
        assertEquals("content://asdf/preferences", uri.toString());
    }

    public void testGenerateInternalContentUri() throws Exception {
        Uri uri = TrayContract.generateInternalContentUri(getContext());
        assertEquals("content://net.grandcentrix.tray.test/internal_preferences",
                uri.toString());

        TrayContract.sAuthority = "blubb";
        uri = TrayContract.generateInternalContentUri(getContext());
        assertEquals("content://blubb/internal_preferences", uri.toString());
    }

    public void testGenerateInternalContentUri_WithoutProvider_AppShouldCrash()
            throws Exception {

        try {
            TrayContract.generateInternalContentUri(getProviderMockContext());
            fail("did not throw");
        } catch (TrayRuntimeException e) {
            assertTrue(e.getMessage().contains("Internal tray error"));
        }
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        TrayContract.sAuthority = null;
    }
}
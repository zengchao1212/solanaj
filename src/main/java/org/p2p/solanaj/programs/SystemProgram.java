package org.p2p.solanaj.programs;

import org.p2p.solanaj.core.AccountMeta;
import org.p2p.solanaj.core.PublicKey;
import org.p2p.solanaj.core.TransactionInstruction;

import java.util.ArrayList;

import static org.bitcoinj.core.Utils.int64ToByteArrayLE;
import static org.bitcoinj.core.Utils.uint32ToByteArrayLE;

public class SystemProgram extends Program {
    public static final PublicKey PROGRAM_ID = new PublicKey("11111111111111111111111111111111");

    public static final int PROGRAM_INDEX_CREATE_ACCOUNT = 0;
    public static final int PROGRAM_INDEX_TRANSFER = 2;
    public static final int ADVANCE_NONCE_ACCOUNT = 4;
    public static final int INITIALIZE_NONCE_ACCOUNT = 6;

    public static TransactionInstruction transfer(PublicKey fromPublicKey, PublicKey toPublickKey, long lamports) {
        ArrayList<AccountMeta> keys = new ArrayList<AccountMeta>();
        keys.add(new AccountMeta(fromPublicKey, true, true));
        keys.add(new AccountMeta(toPublickKey, false, true));

        // 4 byte instruction index + 8 bytes lamports
        byte[] data = new byte[4 + 8];
        uint32ToByteArrayLE(PROGRAM_INDEX_TRANSFER, data, 0);
        int64ToByteArrayLE(lamports, data, 4);

        return createTransactionInstruction(PROGRAM_ID, keys, data);
    }

    public static TransactionInstruction createAccount(PublicKey fromPublicKey, PublicKey newAccountPublickey,
            long lamports, long space, PublicKey programId) {
        ArrayList<AccountMeta> keys = new ArrayList<AccountMeta>();
        keys.add(new AccountMeta(fromPublicKey, true, true));
        keys.add(new AccountMeta(newAccountPublickey, true, true));

        byte[] data = new byte[4 + 8 + 8 + 32];
        uint32ToByteArrayLE(PROGRAM_INDEX_CREATE_ACCOUNT, data, 0);
        int64ToByteArrayLE(lamports, data, 4);
        int64ToByteArrayLE(space, data, 12);
        System.arraycopy(programId.toByteArray(), 0, data, 20, 32);

        return createTransactionInstruction(PROGRAM_ID, keys, data);
    }

    public static TransactionInstruction nonceInitialize(PublicKey noncePub, PublicKey authorizedPub) {
        ArrayList<AccountMeta> keys = new ArrayList<AccountMeta>();
        keys.add(new AccountMeta(noncePub, false, true));
        keys.add(new AccountMeta(SYSVAR_RECENT_BLOCKHASHES_PUBKEY, false, false));
        keys.add(new AccountMeta(SYSVAR_RENT_PUBKEY, false, false));
        byte[] data = new byte[4 + 32];
        uint32ToByteArrayLE(INITIALIZE_NONCE_ACCOUNT, data, 0);
        System.arraycopy(authorizedPub.toByteArray(), 0, data, 4, 32);
        return createTransactionInstruction(PROGRAM_ID, keys, data);
    }

    public static TransactionInstruction nonceAdvance(PublicKey noncePub, PublicKey authorizedPub) {
        ArrayList<AccountMeta> keys = new ArrayList<AccountMeta>();
        keys.add(new AccountMeta(noncePub, false, true));
        keys.add(new AccountMeta(SYSVAR_RECENT_BLOCKHASHES_PUBKEY, false, false));
        keys.add(new AccountMeta(authorizedPub, true, false));
        byte[] data = new byte[4];
        uint32ToByteArrayLE(ADVANCE_NONCE_ACCOUNT, data, 0);
        return createTransactionInstruction(PROGRAM_ID, keys, data);
    }
}

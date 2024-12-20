package org.p2p.solanaj.core;

import com.google.common.primitives.Bytes;
import lombok.Data;
import org.bitcoinj.core.Base58;

import java.math.BigInteger;
import java.nio.ByteBuffer;

@Data
public class NonceAccount {
    public static int NONCE_ACCOUNT_DATA_LEN = 80;
    private final long version;
    private final long state;
    private final String authorizedPubkey;
    private final String nonce;
    private final BigInteger feeCalculator;

    private NonceAccount(long version, long state, String authorizedPubkey, String nonce, BigInteger feeCalculator) {
        this.version = version;
        this.state = state;
        this.authorizedPubkey = authorizedPubkey;
        this.nonce = nonce;
        this.feeCalculator = feeCalculator;
    }

    public static NonceAccount fromAccountData(byte[] accountData) {
        if (accountData.length != NONCE_ACCOUNT_DATA_LEN) {
            throw new IllegalArgumentException("Invalid nonce account data");
        }
        ByteBuffer buffer = ByteBuffer.wrap(accountData);
        byte[] version = new byte[4];
        buffer.get(version);
        Bytes.reverse(version);
        byte[] state = new byte[4];
        buffer.get(state);
        Bytes.reverse(state);
        byte[] authorizedPubkey = new byte[32];
        buffer.get(authorizedPubkey);
        byte[] nonce = new byte[32];
        buffer.get(nonce);
        byte[] feeCalculator = new byte[8];
        buffer.get(feeCalculator);
        Bytes.reverse(feeCalculator);
        return new NonceAccount(new BigInteger(version).longValue(), new BigInteger(state).longValue(), Base58.encode(authorizedPubkey), Base58.encode(nonce), new BigInteger(feeCalculator));
    }

}

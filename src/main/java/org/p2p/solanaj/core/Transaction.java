package org.p2p.solanaj.core;

import lombok.Setter;
import org.bitcoinj.core.Base58;
import org.p2p.solanaj.utils.ShortvecEncoding;
import org.p2p.solanaj.utils.TweetNaclFast;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Transaction {

    public static final int SIGNATURE_LENGTH = 64;

    private Message message;
    private List<String> signatures;
    private byte[] serializedMessage;
    @Setter
    private PublicKey feePayer;

    public Transaction() {
        this.message = new Message();
        this.signatures = new ArrayList<String>();
    }

    public Transaction addInstruction(TransactionInstruction instruction) {
        message.addInstruction(instruction);

        return this;
    }

    public void setRecentBlockHash(String recentBlockhash) {
        message.setRecentBlockHash(recentBlockhash);
    }

    public void sign(Account signer) {
        sign(Arrays.asList(signer));
    }

    public void sign(List<Account> signers) {

        if (signers.size() == 0) {
            throw new IllegalArgumentException("No signers");
        }

        Account feePayer = signers.get(0);
        message.setFeePayer(feePayer.getPublicKey());

        serializedMessage = message.serialize();

        for (Account signer : signers) {
            TweetNaclFast.Signature signatureProvider = new TweetNaclFast.Signature(new byte[0], signer.getSecretKey());
            byte[] signature = signatureProvider.detached(serializedMessage);

            signatures.add(Base58.encode(signature));
        }
    }

    public byte[] serialize() {
        int signaturesSize = signatures.size();
        byte[] signaturesLength = ShortvecEncoding.encodeLength(signaturesSize);

        ByteBuffer out = ByteBuffer
                .allocate(signaturesLength.length + signaturesSize * SIGNATURE_LENGTH + serializedMessage.length);

        out.put(signaturesLength);

        for (String signature : signatures) {
            byte[] rawSignature = Base58.decode(signature);
            out.put(rawSignature);
        }

        out.put(serializedMessage);

        return out.array();
    }

    public String getSignature() {
        return this.signatures.size() > 0 ? (String) this.signatures.get(0) : null;
    }

    public byte[] serializeMessage() {
        if (this.serializedMessage == null) {
            message.setFeePayer(feePayer);
            this.serializedMessage = message.serialize();
        }

        return this.serializedMessage;
    }

    public Transaction addSignature(String signature) {
        this.signatures.add(signature);
        return this;
    }
}

package org.p2p.solanaj.programs;

import org.bouncycastle.util.encoders.Hex;
import org.junit.Test;
import org.p2p.solanaj.core.Account;
import org.p2p.solanaj.core.PublicKey;
import org.p2p.solanaj.core.Transaction;
import org.p2p.solanaj.utils.TweetNaclFast;

import java.util.Base64;
import java.util.List;

public class SystemProgramTest {


    @Test
    public void nonceAccountInit() {
        Account nonce = new Account();
        PublicKey nonceAccount = nonce.getPublicKey();
        TweetNaclFast.Signature.KeyPair keyPair = TweetNaclFast.Signature.keyPair_fromSeed(Hex.decode(""));
        Account owner = new Account(keyPair);
        PublicKey ownerAccount = owner.getPublicKey();
        int lamports = 2039280;
        Transaction transaction = new Transaction();
        transaction.addInstruction(SystemProgram.createAccount(ownerAccount, nonceAccount, lamports, 80, SystemProgram.PROGRAM_ID));
        transaction.addInstruction(SystemProgram.nonceInitialize(nonceAccount, ownerAccount));
        transaction.setRecentBlockHash("GdkE8GSLbojaHSr4wk3rfeym9L58KVTc8RjRK47AY87j");
        transaction.sign(List.of(owner, nonce));
        System.out.println(Base64.getEncoder().encodeToString(transaction.serialize()));
    }

    @Test
    public void transfer() {
        TweetNaclFast.Signature.KeyPair keyPair = TweetNaclFast.Signature.keyPair_fromSeed(Hex.decode(""));
        Account owner = new Account(keyPair);
        PublicKey ownerAccount = owner.getPublicKey();
        PublicKey nonceAccount = new PublicKey("Emc6xjwdsTPAGNPHb9tgU56dNoE5gTmdTAVgLFPheQrq");
        PublicKey toAccount = new PublicKey("2LAJoPdc1bVhi92Ffb5bitR2paWUhHg9DX1AThXPEFLR");
        Transaction transaction = new Transaction();
        transaction.addInstruction(SystemProgram.nonceAdvance(nonceAccount, ownerAccount));
        transaction.addInstruction(SystemProgram.transfer(ownerAccount, toAccount, 200000000));
        transaction.setRecentBlockHash("ArsyuaL766xxLjw5bd7e9W87cyy2nt4PQuHN71MSUVjr");
        transaction.sign(owner);
        System.out.println(Base64.getEncoder().encodeToString(transaction.serialize()));
    }

}

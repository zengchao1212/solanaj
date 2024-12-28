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
//        byte[] xx=Base64.getDecoder().decode("AQAAAAEAAACmZ0+HDScYVIGEslhK6HZW32hxRJmhrnN7RrfAyxVw8hV1lA4gcgoUX4r/Vl9LV2vzLjzSvpzxV5bj8t+XOOAAiBMAAAAAAAA=");
//        byte[] oo= Base58.decode("2SmYpGTJMHrjb5pgu3NeYHt8T7eCVpB1zU5cj9uQBoEP");
//        byte[] blockHash=new byte[32];
//        System.arraycopy(xx,40,blockHash,0,32);
//        System.out.println(Base58.encode(blockHash));
        TweetNaclFast.Signature.KeyPair keyPair = TweetNaclFast.Signature.keyPair_fromSeed(Hex.decode("332521042b175293ee717df015b3a755bbbc5f8d9f53b258f0478608daec5263"));
        Account owner = new Account(keyPair);
        PublicKey ownerAccount = owner.getPublicKey();
        PublicKey toAccount = new PublicKey("2YHijnm5JP1a4UKrmnXstMvDSq2xPeyKyfHqgSYD5LLk");
//        PublicKey nonceAccount = new PublicKey("Emc6xjwdsTPAGNPHb9tgU56dNoE5gTmdTAVgLFPheQrq");
//        PublicKey toAccount = new PublicKey("2LAJoPdc1bVhi92Ffb5bitR2paWUhHg9DX1AThXPEFLR");
        Transaction transaction = new Transaction();
//        transaction.addInstruction(SystemProgram.nonceAdvance(nonceAccount, ownerAccount));
        transaction.addInstruction(SystemProgram.transfer(ownerAccount, toAccount, 2000000));
//        transaction.setRecentBlockHash("4h1q7CvmetRnPtGzCqYvZtJVM9UsM5iLBoP2kneBaphd");
        transaction.sign(owner);
        System.out.println(Base64.getEncoder().encodeToString(transaction.serialize()));
    }

}

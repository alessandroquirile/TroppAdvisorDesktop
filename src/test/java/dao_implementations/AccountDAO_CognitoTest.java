package dao_implementations;

import models.Account;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Alessandro Quirile, Mauro Telese
 */
public class AccountDAO_CognitoTest {
    private Account account;
    private AccountDAO_Cognito accountDAO_cognito;

    @Before
    public void setUp() {
        account = new Account();
        accountDAO_cognito = new AccountDAO_Cognito();
    }

    // Black-Box Testing, metodologia Strong Equivalence Class Testing (SECT)

    // Copre EC1 - EC6
    @Test
    public void loginEmailVuotaPasswordVuota() throws IOException, InterruptedException {
        account.setEmail("");
        account.setPassword("");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC1 - EC7
    @Test
    public void loginEmailVuotaPasswordNull() throws IOException, InterruptedException {
        account.setEmail("");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC1 - EC8
    @Test
    public void loginEmailVuotaPasswordMinoreDi8CaratteriSenzaMaiuscoleSenzaMinuscoleSenzaCaratteriSpecialiSenzaNumeri()
            throws IOException, InterruptedException {
        account.setEmail("");
        account.setPassword(" ");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC1 - EC9
    @Test
    public void loginEmailVuotaPasswordMinoreDi8CaratteriSoloNumeri() throws IOException, InterruptedException {
        account.setEmail("");
        account.setPassword("1234567");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC1 - EC10
    @Test
    public void loginEmailVuotaPasswordMinoreDi8CaratteriSoloCaratteriSpeciali() throws IOException, InterruptedException {
        account.setEmail("");
        account.setPassword("@.&.@@@");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC1 - EC11
    @Test
    public void loginEmailVuotaPasswordMinoreDi8CaratteriSoloNumeriECaratteriSpeciali() throws IOException, InterruptedException {
        account.setEmail("");
        account.setPassword("123456@");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC1 - EC12
    @Test
    public void loginEmailVuotaPasswordMinoreDi8CaratteriSoloMinuscole() throws IOException, InterruptedException {
        account.setEmail("");
        account.setPassword("abcdefg");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC1 - EC13
    @Test
    public void loginEmailVuotaPasswordMinoreDi8CaratteriSoloMinuscoleENumeri() throws IOException, InterruptedException {
        account.setEmail("");
        account.setPassword("abcdef7");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC1 - EC14
    @Test
    public void loginEmailVuotaPasswordMinoreDi8CaratteriSoloMinuscoleECaratteriSpeciali() throws IOException, InterruptedException {
        account.setEmail("");
        account.setPassword("abcdef@");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC1 - EC15
    @Test
    public void loginEmailVuotaPasswordMinoreDi8CaratteriSenzaMaiuscole() throws IOException, InterruptedException {
        account.setEmail("");
        account.setPassword("ab3def@");
        account.setPassword("ab3def@");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC1 - EC16
    @Test
    public void loginEmailVuotaPasswordMinoreDi8CaratteriSoloMaiuscole() throws IOException, InterruptedException {
        account.setEmail("");
        account.setPassword("ABCDEFG");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC1 - EC17
    @Test
    public void loginEmailVuotaPasswordMinoreDi8CaratteriSoloMaiuscoleENumeri() throws IOException, InterruptedException {
        account.setEmail("");
        account.setPassword("ABCDEF7");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC1 - EC18
    @Test
    public void loginEmailVuotaPasswordMinoreDi8CaratteriSoloMaiuscoleECaratteriSpeciali() throws IOException, InterruptedException {
        account.setEmail("");
        account.setPassword("ABCDEF@");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC1 - EC19
    @Test
    public void loginEmailVuotaPasswordMinoreDi8CaratteriSenzaMinuscole() throws IOException, InterruptedException {
        account.setEmail("");
        account.setPassword("AB3DEF@");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC1 - EC20
    @Test
    public void loginEmailVuotaPasswordMinoreDi8CaratteriSoloMaiuscoleEMinuscole() throws IOException, InterruptedException {
        account.setEmail("");
        account.setPassword("ABCdef");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC1 - EC21
    @Test
    public void loginEmailVuotaPasswordMinoreDi8CaratteriSenzaCaratteriSpeciali() throws IOException, InterruptedException {
        account.setEmail("");
        account.setPassword("ABCdef7");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC1 - EC22
    @Test
    public void loginEmailVuotaPasswordMinoreDi8CaratteriSenzaNumeri() throws IOException, InterruptedException {
        account.setEmail("");
        account.setPassword("ABCdef@");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC1 - EC23
    @Test
    public void loginEmailVuotaPasswordMinoreDi8Caratteri() throws IOException, InterruptedException {
        account.setEmail("");
        account.setPassword("       ");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC1 - EC24
    @Test
    public void loginEmailVuotaPasswordMaggioreUgualeDi8Caratteri() throws IOException, InterruptedException {
        account.setEmail("");
        account.setPassword("        ");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC1 - EC25
    @Test
    public void loginEmailVuotaPasswordMaggioreUgualeDi8CaratteriSoloNumeri() throws IOException, InterruptedException {
        account.setEmail("");
        account.setPassword("12345678");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC1 - EC26
    @Test
    public void loginEmailVuotaPasswordMaggioreUgualeDi8CaratteriSoloCaratteriSpeciali() throws IOException, InterruptedException {
        account.setEmail("");
        account.setPassword("@&@.$&€.");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC1 - EC27
    @Test
    public void loginEmailVuotaPasswordMaggioreUgualeDi8CaratteriSoloCaratteriSpecialiENumeri() throws IOException, InterruptedException {
        account.setEmail("");
        account.setPassword("1&3.$&€.");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC1 - EC28
    @Test
    public void loginEmailVuotaPasswordMaggioreUgualeDi8CaratteriSoloMinuscole() throws IOException, InterruptedException {
        account.setEmail("");
        account.setPassword("abcdefgh");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC1 - EC29
    @Test
    public void loginEmailVuotaPasswordMaggioreUgualeDi8CaratteriSoloMinuscoleENumeri() throws IOException, InterruptedException {
        account.setEmail("");
        account.setPassword("abcdefg8");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC1 - EC30
    @Test
    public void loginEmailVuotaPasswordMaggioreUgualeDi8CaratteriSoloMinuscoleECaratteriSpeciali() throws IOException, InterruptedException {
        account.setEmail("");
        account.setPassword("abcdefg@");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC1 - EC31
    @Test
    public void loginEmailVuotaPasswordMaggioreUgualeDi8CaratteriSenzaMaiuscole() throws IOException, InterruptedException {
        account.setEmail("");
        account.setPassword("ab3defg@");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC1 - EC32
    @Test
    public void loginEmailVuotaPasswordMaggioreUgualeDi8CaratteriSoloMaiuscole() throws IOException, InterruptedException {
        account.setEmail("");
        account.setPassword("ABCDEFGH");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC1 - EC33
    @Test
    public void loginEmailVuotaPasswordMaggioreUgualeDi8CaratteriSoloMaiuscoleENumeri() throws IOException, InterruptedException {
        account.setEmail("");
        account.setPassword("ABCDEFG8");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC1 - EC34
    @Test
    public void loginEmailVuotaPasswordMaggioreUgualeDi8CaratteriSoloMaiuscoleECaratteriSpeciali() throws IOException, InterruptedException {
        account.setEmail("");
        account.setPassword("ABCDEFG@");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC1 - EC35
    @Test
    public void loginEmailVuotaPasswordMaggioreUgualeDi8CaratteriSenzaMinuscole() throws IOException, InterruptedException {
        account.setEmail("");
        account.setPassword("AB3DEFG@");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC1 - EC36
    @Test
    public void loginEmailVuotaPasswordMaggioreUgualeDi8CaratteriSoloMaiuscoleEMinuscole() throws IOException, InterruptedException {
        account.setEmail("");
        account.setPassword("ABcDEFGh");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC1 - EC37
    @Test
    public void loginEmailVuotaPasswordMaggioreUgualeDi8CaratteriSenzaNumeri() throws IOException, InterruptedException {
        account.setEmail("");
        account.setPassword("ABcDEFG@");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC1 - EC38
    @Test
    public void loginEmailVuotaPasswordMaggioreUgualeDi8CaratteriConMaiuscoleMinuscoleCaratteriSpecialiENumeri()
            throws IOException, InterruptedException {
        account.setEmail("");
        account.setPassword("1BcDEFG@");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC1 - EC39
    @Test
    public void loginEmailVuotaPasswordMaggioreUgualeDi8CaratteriConMaiuscoleMinuscoleCaratteriSpecialiENumeriMaNonPresenteNelDB()
            throws IOException, InterruptedException {
        account.setEmail("");
        account.setPassword("1BcDEFG@");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC1 - EC40
    @Test
    public void loginEmailVuotaPasswordMaggioreUgualeDi8CaratteriConMaiuscoleMinuscoleCaratteriSpecialiENumeriEPresenteNelDB()
            throws IOException, InterruptedException {
        account.setEmail("");
        account.setPassword("Pass.word1");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC2 - EC6
    @Test
    public void loginEmailNullPasswordVuota() throws IOException, InterruptedException {
        account.setEmail(null);
        account.setPassword("");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC2 - EC7
    @Test
    public void loginEmailNullPasswordNull() throws IOException, InterruptedException {
        account.setEmail(null);
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC2 - EC8
    @Test
    public void loginEmailNullPasswordMinoreDi8CaratteriSenzaMaiuscoleSenzaMinuscoleSenzaCaratteriSpecialiSenzaNumeri()
            throws IOException, InterruptedException {
        account.setEmail(null);
        account.setPassword(" ");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC2 - EC9
    @Test
    public void loginEmailNullPasswordMinoreDi8CaratteriSoloNumeri() throws IOException, InterruptedException {
        account.setEmail(null);
        account.setPassword("1234567");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC2 - EC10
    @Test
    public void loginEmailNullPasswordMinoreDi8CaratteriSoloCaratteriSpeciali() throws IOException, InterruptedException {
        account.setEmail(null);
        account.setPassword("@.&.@@@");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC2 - EC11
    @Test
    public void loginEmailNullPasswordMinoreDi8CaratteriSoloCaratteriSpecialiENumeri() throws IOException, InterruptedException {
        account.setEmail(null);
        account.setPassword("@.&.@@7");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC2 - EC12
    @Test
    public void loginEmailNullPasswordMinoreDi8CaratteriSoloMinuscole() throws IOException, InterruptedException {
        account.setEmail(null);
        account.setPassword("abcdefg");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC2 - EC13
    @Test
    public void loginEmailNullPasswordMinoreDi8CaratteriSoloMinuscoleENumeri() throws IOException, InterruptedException {
        account.setEmail(null);
        account.setPassword("abcdef7");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC2 - EC14
    @Test
    public void loginEmailNullPasswordMinoreDi8CaratteriSoloMinuscoleECaratterispeciali() throws IOException, InterruptedException {
        account.setEmail(null);
        account.setPassword("abcdef@");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC2 - EC15
    @Test
    public void loginEmailNullPasswordMinoreDi8CaratteriSenzaMaiuscole() throws IOException, InterruptedException {
        account.setEmail(null);
        account.setPassword("ab3def@");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC2 - EC16
    @Test
    public void loginEmailNullPasswordMinoreDi8CaratteriSoloMaiuscole() throws IOException, InterruptedException {
        account.setEmail(null);
        account.setPassword("ABCDEFG");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC2 - EC17
    @Test
    public void loginEmailNullPasswordMinoreDi8CaratteriSoloMaiuscoleENumeri() throws IOException, InterruptedException {
        account.setEmail(null);
        account.setPassword("ABCDEF7");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC2 - EC18
    @Test
    public void loginEmailNullPasswordMinoreDi8CaratteriSoloMaiuscoleECaratteriSpeciali() throws IOException, InterruptedException {
        account.setEmail(null);
        account.setPassword("ABCDEF@");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC2 - EC19
    @Test
    public void loginEmailNullPasswordMinoreDi8CaratteriSenzaMinuscole() throws IOException, InterruptedException {
        account.setEmail(null);
        account.setPassword("AB3DEF@");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC2 - EC20
    @Test
    public void loginEmailNullPasswordMinoreDi8CaratteriSoloMaiuscoleEMinuscole() throws IOException, InterruptedException {
        account.setEmail(null);
        account.setPassword("ABCdef");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC2 - EC21
    @Test
    public void loginEmailNullPasswordMinoreDi8CaratteriSenzaCaratteriSpeciali() throws IOException, InterruptedException {
        account.setEmail(null);
        account.setPassword("ABCdef7");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC2 - EC22
    @Test
    public void loginEmailNullPasswordMinoreDi8CaratteriSenzaNumeri() throws IOException, InterruptedException {
        account.setEmail(null);
        account.setPassword("ABCdef@");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC2 - EC23
    @Test
    public void loginEmailNullPasswordMinoreDi8Caratteri() throws IOException, InterruptedException {
        account.setEmail(null);
        account.setPassword("       ");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC2 - EC24
    @Test
    public void loginEmailNullPasswordMaggioreUgualeDi8Caratteri() throws IOException, InterruptedException {
        account.setEmail(null);
        account.setPassword("        ");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC2 - EC25
    @Test
    public void loginEmailNullPasswordMaggioreUgualeDi8CaratteriSoloNumeri() throws IOException, InterruptedException {
        account.setEmail(null);
        account.setPassword("12345678");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC2 - EC26
    @Test
    public void loginEmailNullPasswordMaggioreUgualeDi8CaratteriSoloCaratteriSpeciali() throws IOException, InterruptedException {
        account.setEmail(null);
        account.setPassword("@&@.$&€.");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC2 - EC27
    @Test
    public void loginEmailNullPasswordMaggioreUgualeDi8CaratteriSoloCaratteriSpecialiENumeri() throws IOException, InterruptedException {
        account.setEmail(null);
        account.setPassword("@&@.$&€8");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC2 - EC28
    @Test
    public void loginEmailNullPasswordMaggioreUgualeDi8CaratteriSoloMinuscole() throws IOException, InterruptedException {
        account.setEmail(null);
        account.setPassword("abcdefgh");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC2 - EC29
    @Test
    public void loginEmailNullPasswordMaggioreUgualeDi8CaratteriSoloMinuscoleENumeri() throws IOException, InterruptedException {
        account.setEmail(null);
        account.setPassword("abcdefg8");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC2 - EC30
    @Test
    public void loginEmailNullPasswordMaggioreUgualeDi8CaratteriSoloMinuscoleECaratteriSpeciali() throws IOException, InterruptedException {
        account.setEmail(null);
        account.setPassword("abcdefg@");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC2 - EC31
    @Test
    public void loginEmailNullPasswordMaggioreUgualeDi8CaratteriSenzaMaiuscole() throws IOException, InterruptedException {
        account.setEmail(null);
        account.setPassword("ab3defg@");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC2 - EC32
    @Test
    public void loginEmailNullPasswordMaggioreUgualeDi8CaratteriSoloMaiuscole() throws IOException, InterruptedException {
        account.setEmail(null);
        account.setPassword("ABCDEFGH");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC2 - EC33
    @Test
    public void loginEmailNullPasswordMaggioreUgualeDi8CaratteriSoloMaiuscoleENumeri() throws IOException, InterruptedException {
        account.setEmail(null);
        account.setPassword("ABCDEFG8");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC2 - EC34
    @Test
    public void loginEmailNullPasswordMaggioreUgualeDi8CaratteriSoloMaiuscoleECaratteriSpeciali() throws IOException, InterruptedException {
        account.setEmail(null);
        account.setPassword("ABCDEFG@");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC2 - EC35
    @Test
    public void loginEmailNullPasswordMaggioreUgualeDi8CaratteriSenzaMinuscole() throws IOException, InterruptedException {
        account.setEmail(null);
        account.setPassword("AB3DEFG@");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC2 - EC36
    @Test
    public void loginEmailNullPasswordMaggioreUgualeDi8CaratteriSoloMaiuscoleEMinuscole() throws IOException, InterruptedException {
        account.setEmail(null);
        account.setPassword("ABcDEFGh");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC2 - EC37
    @Test
    public void loginEmailNullPasswordMaggioreUgualeDi8CaratteriSenzaNumeri() throws IOException, InterruptedException {
        account.setEmail(null);
        account.setPassword("ABcDEFG@");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC2 - EC38
    @Test
    public void loginEmailNullPasswordMaggioreUgualeDi8CaratteriConMaiuscoleMinuscoleCaratteriSpecialiENumeri()
            throws IOException, InterruptedException {
        account.setEmail(null);
        account.setPassword("ABcDEFG@");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC2 - EC39
    @Test
    public void loginEmailNullPasswordMaggioreUgualeDi8CaratteriConMaiuscoleMinuscoleCaratteriSpecialiENumeriMaNonPresenteNelDB()
            throws IOException, InterruptedException {
        account.setEmail(null);
        account.setPassword("1BcDEFG@");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC2 - EC40
    @Test
    public void loginEmailNullPasswordMaggioreUgualeDi8CaratteriConMaiuscoleMinuscoleCaratteriSpecialiENumeriEPresenteNelDB()
            throws IOException, InterruptedException {
        account.setEmail(null);
        account.setPassword("Pass.word1");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC3 - EC6
    @Test
    public void loginEmailSintassiErrataPasswordVuota() throws IOException, InterruptedException {
        account.setEmail("alessandro.quirile@.it");
        account.setPassword("");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC3 - EC7
    @Test
    public void loginEmailSintassiErrataPasswordNull() throws IOException, InterruptedException {
        account.setEmail("alessandro.quirile@.it");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC3 - EC8
    @Test
    public void loginEmailSintassiErrataPasswordMinoreDi8CaratteriSenzaMaiuscoleSenzaMinuscoleSenzaCaratteriSpecialiSenzaNumeri()
            throws IOException, InterruptedException {
        account.setEmail("alessandro.quirile@.it");
        account.setPassword(" ");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC3 - EC9
    @Test
    public void loginEmailSintassiErrataPasswordMinoreDi8CaratteriSoloNumeri() throws IOException, InterruptedException {
        account.setEmail("alessandro.quirile@.it");
        account.setPassword("1234567");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC3 - EC10
    @Test
    public void loginEmailSintassiErrataPasswordMinoreDi8CaratteriSoloCaratteriSpeciali() throws IOException, InterruptedException {
        account.setEmail("alessandro.quirile@.it");
        account.setPassword("@.&.@@@");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC3 - EC11
    @Test
    public void loginEmailSintassiErrataPasswordMinoreDi8CaratteriSoloCaratteriSpecialiENumeri() throws IOException, InterruptedException {
        account.setEmail("alessandro.quirile@.it");
        account.setPassword("@.&.@@7");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC3 - EC12
    @Test
    public void loginEmailSintassiErrataPasswordMinoreDi8CaratteriSoloMinuscole() throws IOException, InterruptedException {
        account.setEmail("alessandro.quirile@.it");
        account.setPassword("abcdefg");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC3 - EC13
    @Test
    public void loginEmailSintassiErrataPasswordMinoreDi8CaratteriSoloMinuscoleENumeri() throws IOException, InterruptedException {
        account.setEmail("alessandro.quirile@.it");
        account.setPassword("abcdef7");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC3 - EC14
    @Test
    public void loginEmailSintassiErrataPasswordMinoreDi8CaratteriSoloMinuscoleECaratteriSpeciali()
            throws IOException, InterruptedException {
        account.setEmail("alessandro.quirile@.it");
        account.setPassword("abcdef@");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC3 - EC15
    @Test
    public void loginEmailSintassiErrataPasswordMinoreDi8CaratteriSenzaMaiuscole() throws IOException, InterruptedException {
        account.setEmail("alessandro.quirile@.it");
        account.setPassword("ab3def@");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC3 - EC16
    @Test
    public void loginEmailSintassiErrataPasswordMinoreDi8CaratteriSoloMaiuscole() throws IOException, InterruptedException {
        account.setEmail("alessandro.quirile@.it");
        account.setPassword("ABCDEFG");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC3 - EC17
    @Test
    public void loginEmailSintassiErrataPasswordMinoreDi8CaratteriSoloMaiuscoleENumeri() throws IOException, InterruptedException {
        account.setEmail("alessandro.quirile@.it");
        account.setPassword("ABCDEF7");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC3 - EC18
    @Test
    public void loginEmailSintassiErrataPasswordMinoreDi8CaratteriSoloMaiuscoleECaratteriSpeciali() throws IOException, InterruptedException {
        account.setEmail("alessandro.quirile@.it");
        account.setPassword("ABCDEF@");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC3 - EC19
    @Test
    public void loginEmailSintassiErrataPasswordMinoreDi8CaratteriSenzaMinuscole() throws IOException, InterruptedException {
        account.setEmail("alessandro.quirile@.it");
        account.setPassword("AB3DEF@");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC3 - EC20
    @Test
    public void loginEmailSintassiErrataPasswordMinoreDi8CaratteriSoloMaiuscoleEMinuscole() throws IOException, InterruptedException {
        account.setEmail("alessandro.quirile@.it");
        account.setPassword("ABCdefg");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC3 - EC21
    @Test
    public void loginEmailSintassiErrataPasswordMinoreDi8CaratteriSenzaCaratteriSpeciali() throws IOException, InterruptedException {
        account.setEmail("alessandro.quirile@.it");
        account.setPassword("ABCdef7");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC3 - EC22
    @Test
    public void loginEmailSintassiErrataPasswordMinoreDi8CaratteriSenzaNumeri() throws IOException, InterruptedException {
        account.setEmail("alessandro.quirile@.it");
        account.setPassword("ABCdef@");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC3 - EC23
    @Test
    public void loginEmailSintassiErrataPasswordMinoreDi8Caratteri() throws IOException, InterruptedException {
        account.setEmail("alessandro.quirile@.it");
        account.setPassword("       ");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC3 - EC24
    @Test
    public void loginEmailSintassiErrataPasswordMaggioreUgualeDi8Caratteri() throws IOException, InterruptedException {
        account.setEmail("alessandro.quirile@.it");
        account.setPassword("        ");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC3 - EC25
    @Test
    public void loginEmailSintassiErrataPasswordMaggioreUgualeDi8CaratteriSoloNumeri() throws IOException, InterruptedException {
        account.setEmail("alessandro.quirile@.it");
        account.setPassword("12345678");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC3 - EC26
    @Test
    public void loginEmailSintassiErrataPasswordMaggioreUgualeDi8CaratteriSoloCaratteriSpeciali() throws IOException, InterruptedException {
        account.setEmail("alessandro.quirile@.it");
        account.setPassword("@&@.$&€.");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC3 - EC27
    @Test
    public void loginEmailSintassiErrataPasswordMaggioreUgualeDi8CaratteriSoloCaratteriSpecialiENumeri()
            throws IOException, InterruptedException {
        account.setEmail("alessandro.quirile@.it");
        account.setPassword("@&@.$&€8");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC3 - EC28
    @Test
    public void loginEmailSintassiErrataPasswordMaggioreUgualeDi8CaratteriSoloMinuscole() throws IOException, InterruptedException {
        account.setEmail("alessandro.quirile@.it");
        account.setPassword("abcdefgh");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC3 - EC29
    @Test
    public void loginEmailSintassiErrataPasswordMaggioreUgualeDi8CaratteriSoloMinuscoleENumeri() throws IOException, InterruptedException {
        account.setEmail("alessandro.quirile@.it");
        account.setPassword("abcdefg8");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC3 - EC30
    @Test
    public void loginEmailSintassiErrataPasswordMaggioreUgualeDi8CaratteriSoloMinuscoleECaratteriSpeciali()
            throws IOException, InterruptedException {
        account.setEmail("alessandro.quirile@.it");
        account.setPassword("abcdefg@");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC3 - EC31
    @Test
    public void loginEmailSintassiErrataPasswordMaggioreUgualeDi8CaratteriSenzaMaiuscole() throws IOException, InterruptedException {
        account.setEmail("alessandro.quirile@.it");
        account.setPassword("ab3defg@");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC3 - EC32
    @Test
    public void loginEmailSintassiErrataPasswordMaggioreUgualeDi8CaratteriSoloMaiuscole() throws IOException, InterruptedException {
        account.setEmail("alessandro.quirile@.it");
        account.setPassword("ABCDEFGH");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC3 - EC33
    @Test
    public void loginEmailSintassiErrataPasswordMaggioreUgualeDi8CaratteriSoloMaiuscoleENumeri() throws IOException, InterruptedException {
        account.setEmail("alessandro.quirile@.it");
        account.setPassword("ABCDEFG8");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC3 - EC34
    @Test
    public void loginEmailSintassiErrataPasswordMaggioreUgualeDi8CaratteriSoloMaiuscoleECaratteriSpeciali()
            throws IOException, InterruptedException {
        account.setEmail("alessandro.quirile@.it");
        account.setPassword("ABCDEFG@");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC3 - EC35
    @Test
    public void loginEmailSintassiErrataPasswordMaggioreUgualeDi8CaratteriSenzaMinuscole() throws IOException, InterruptedException {
        account.setEmail("alessandro.quirile@.it");
        account.setPassword("AB3DEFG@");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC3 - EC36
    @Test
    public void loginEmailSintassiErrataPasswordMaggioreUgualeDi8CaratteriSoloMaiuscoleEMinuscole() throws IOException, InterruptedException {
        account.setEmail("alessandro.quirile@.it");
        account.setPassword("ABCdefgH");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC3 - EC37
    @Test
    public void loginEmailSintassiErrataPasswordMaggioreUgualeDi8CaratteriSenzaNumeri() throws IOException, InterruptedException {
        account.setEmail("alessandro.quirile@.it");
        account.setPassword("ABcDEFG@");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC3 - EC38
    @Test
    public void loginEmailSintassiErrataPasswordMaggioreUgualeDi8CaratteriConMaiuscoleMinuscoleCaratteriSpecialiENumeri()
            throws IOException, InterruptedException {
        account.setEmail("alessandro.quirile@.it");
        account.setPassword("1BcDEFG@");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC3 - EC39
    @Test
    public void loginEmailSintassiErrataPasswordMaggioreUgualeDi8CaratteriConMaiuscoleMinuscoleCaratteriSpecialiENumeriMaNonPresenteNelDB()
            throws IOException, InterruptedException {
        account.setEmail("alessandro.quirile@.it");
        account.setPassword("1BcDEFG@");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC3 - EC40
    @Test
    public void loginEmailSintassiErrataPasswordMaggioreUgualeDi8CaratteriConMaiuscoleMinuscoleCaratteriSpecialiENumeriEPresenteNelDB()
            throws IOException, InterruptedException {
        account.setEmail("alessandro.quirile@.it");
        account.setPassword("Pass.word1");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC4 - EC6
    @Test
    public void loginEmailNonPresenteNelDBPasswordVuota() throws IOException, InterruptedException {
        account.setEmail("marlon.brando@alice.it");
        account.setPassword("");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC4 - EC7
    @Test
    public void loginEmailNonPresenteNelDBPasswordNull() throws IOException, InterruptedException {
        account.setEmail("marlon.brando@alice.it");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC4 - EC8
    @Test
    public void loginEmailNonPresenteNelDBMinoreDi8CaratteriSenzaMaiuscoleSenzaMinuscoleSenzaCaratteriSpecialiSenzaNumeri()
            throws IOException, InterruptedException {
        account.setEmail("marlon.brando@alice.it");
        account.setPassword(" ");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC4 - EC9
    @Test
    public void loginEmailNonPresenteNelDBPasswordMinoreDi8CaratteriSoloNumeri() throws IOException, InterruptedException {
        account.setEmail("marlon.brando@alice.it");
        account.setPassword("1234567");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC4 - EC10
    @Test
    public void loginEmailNonPresenteNelDBPasswordMinoreDi8CaratteriSoloCaratteriSpeciali() throws IOException, InterruptedException {
        account.setEmail("marlon.brando@alice.it");
        account.setPassword("@@@$%&.");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC4 - EC11
    @Test
    public void loginEmailNonPresenteNelDBPasswordMinoreDi8CaratteriSoloCaratteriSpecialiENumeri() throws IOException, InterruptedException {
        account.setEmail("marlon.brando@alice.it");
        account.setPassword("@@@$%&7");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC4 - EC12
    @Test
    public void loginEmailNonPresenteNelDBPasswordMinoreDi8CaratteriSoloMinuscole() throws IOException, InterruptedException {
        account.setEmail("marlon.brando@alice.it");
        account.setPassword("abcdefg");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC4 - EC13
    @Test
    public void loginEmailNonPresenteNelDBPasswordMinoreDi8CaratteriSoloMinuscoleENumeri() throws IOException, InterruptedException {
        account.setEmail("marlon.brando@alice.it");
        account.setPassword("@@@$%&7");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC4 - EC14
    @Test
    public void loginEmailNonPresenteNelDBPasswordMinoreDi8CaratteriSoloMinuscoleECaratteriSpeciali()
            throws IOException, InterruptedException {
        account.setEmail("marlon.brando@alice.it");
        account.setPassword("abcdef@");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC4 - EC15
    @Test
    public void loginEmailNonPresenteNelDBPasswordMinoreDi8CaratteriSenzaMaiuscole() throws IOException, InterruptedException {
        account.setEmail("marlon.brando@alice.it");
        account.setPassword("ab3def@");
        account.setPassword("ab3def@");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC4 - EC16
    @Test
    public void loginEmailNonPresenteNelDBPasswordMinoreDi8CaratteriSoloMaiuscole() throws IOException, InterruptedException {
        account.setEmail("marlon.brando@alice.it");
        account.setPassword("ABCDEFG");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC4 - EC17
    @Test
    public void loginEmailNonPresenteNelDBPasswordMinoreDi8CaratteriSoloMaiuscoleENumeri() throws IOException, InterruptedException {
        account.setEmail("marlon.brando@alice.it");
        account.setPassword("ABCDEF7");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC4 - EC18
    @Test
    public void loginEmailNonPresenteNelDBPasswordMinoreDi8CaratteriSoloMaiuscoleECaratteriSpeciali() throws IOException, InterruptedException {
        account.setEmail("marlon.brando@alice.it");
        account.setPassword("ABCDEF@");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC4 - EC19
    @Test
    public void loginEmailNonPresenteNelDBPasswordMinoreDi8CaratteriSenzaMinuscole() throws IOException, InterruptedException {
        account.setEmail("marlon.brando@alice.it");
        account.setPassword("AB3DEF@");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC4 - EC20
    @Test
    public void loginEmailNonPresenteNelDBPasswordMinoreDi8CaratteriSoloMaiuscoleEMinuscole() throws IOException, InterruptedException {
        account.setEmail("marlon.brando@alice.it");
        account.setPassword("ABCdefg");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC4 - EC21
    @Test
    public void loginEmailNonPresenteNelDBPasswordMinoreDi8CaratteriSenzaCaratteriSpeciali() throws IOException, InterruptedException {
        account.setEmail("marlon.brando@alice.it");
        account.setPassword("AB3dEF@");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC4 - EC22
    @Test
    public void loginEmailNonPresenteNelDBPasswordMinoreDi8CaratteriSenzaNumeri() throws IOException, InterruptedException {
        account.setEmail("marlon.brando@alice.it");
        account.setPassword("ABCdef@");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC4 - EC23
    @Test
    public void loginEmailNonPresenteNelDBPasswordMinoreDi8Caratteri() throws IOException, InterruptedException {
        account.setEmail("marlon.brando@alice.it");
        account.setPassword("       ");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC4 - EC24
    @Test
    public void loginEmailNonPresenteNelDBPasswordMaggioreUgualeDi8Caratteri() throws IOException, InterruptedException {
        account.setEmail("marlon.brando@alice.it");
        account.setPassword("        ");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC4 - EC25
    @Test
    public void loginEmailNonPresenteNelDBPasswordMaggioreUgualeDi8CaratteriSoloNumeri() throws IOException, InterruptedException {
        account.setEmail("marlon.brando@alice.it");
        account.setPassword("12345678");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC4 - EC26
    @Test
    public void loginEmailNonPresenteNelDBPasswordMaggioreUgualeDi8CaratteriSoloCaratteriSpeciali() throws IOException, InterruptedException {
        account.setEmail("marlon.brando@alice.it");
        account.setPassword("@@$%&.@£");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC4 - EC27
    @Test
    public void loginEmailNonPresenteNelDBPasswordMaggioreUgualeDi8CaratteriSoloCaratteriSpecialiENumeri()
            throws IOException, InterruptedException {
        account.setEmail("marlon.brando@alice.it");
        account.setPassword("1@$%&.@8");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC4 - EC28
    @Test
    public void loginEmailNonPresenteNelDBPasswordMaggioreUgualeDi8CaratteriSoloMinuscole() throws IOException, InterruptedException {
        account.setEmail("marlon.brando@alice.it");
        account.setPassword("abcdefgh");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC4 - EC29
    @Test
    public void loginEmailNonPresenteNelDBPasswordMaggioreUgualeDi8CaratteriSoloMinuscoleENumeri() throws IOException, InterruptedException {
        account.setEmail("marlon.brando@alice.it");
        account.setPassword("abcdefg8");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC4 - EC30
    @Test
    public void loginEmailNonPresenteNelDBPasswordMaggioreUgualeDi8CaratteriSoloMinuscoleECaratteriSpeciali()
            throws IOException, InterruptedException {
        account.setEmail("marlon.brando@alice.it");
        account.setPassword("abcdefg@");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC4 - EC31
    @Test
    public void loginEmailNonPresenteNelDBPasswordMaggioreUgualeDi8CaratteriSenzaMaiuscole() throws IOException, InterruptedException {
        account.setEmail("marlon.brando@alice.it");
        account.setPassword("ab3defg@");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC4 - EC32
    @Test
    public void loginEmailNonPresenteNelDBPasswordMaggioreUgualeDi8CaratteriSoloMaiuscole() throws IOException, InterruptedException {
        account.setEmail("marlon.brando@alice.it");
        account.setPassword("ABCDEFGH");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC4 - EC33
    @Test
    public void loginEmailNonPresenteNelDBPasswordMaggioreUgualeDi8CaratteriSoloMaiuscoleENumeri() throws IOException, InterruptedException {
        account.setEmail("marlon.brando@alice.it");
        account.setPassword("ABCDEFG8");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC4 - EC34
    @Test
    public void loginEmailNonPresenteNelDBPasswordMaggioreUgualeDi8CaratteriSoloMaiuscoleECaratteriSpeciali()
            throws IOException, InterruptedException {
        account.setEmail("marlon.brando@alice.it");
        account.setPassword("ABCDEFG@");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC4 - EC35
    @Test
    public void loginEmailNonPresenteNelDBPasswordMaggioreUgualeDi8CaratteriSenzaMinuscole() throws IOException, InterruptedException {
        account.setEmail("marlon.brando@alice.it");
        account.setPassword("AB3DEFG@");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC4 - EC36
    @Test
    public void loginEmailNonPresenteNelDBPasswordMaggioreUgualeDi8CaratteriSoloMaiuscoleEMinuscole() throws IOException, InterruptedException {
        account.setEmail("marlon.brando@alice.it");
        account.setPassword("aBcDEFGh");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC4 - EC37
    @Test
    public void loginEmailNonPresenteNelDBPasswordMaggioreUgualeDi8CaratteriSenzaNumeri() throws IOException, InterruptedException {
        account.setEmail("marlon.brando@alice.it");
        account.setPassword("ABcDEFG@");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC4 - EC38
    @Test
    public void loginEmailNonPresenteNelDBPasswordMaggioreUgualeDi8CaratteriConMaiuscoleMinuscoleCaratteriSpecialiENumeri()
            throws IOException, InterruptedException {
        account.setEmail("marlon.brando@alice.it");
        account.setPassword("1BcDEFG@");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC4 - EC39
    @Test
    public void loginEmailNonPresenteNelDBPasswordMaggioreUgualeDi8CaratteriConMaiuscoleMinuscoleCaratteriSpecialiENumeriMaNonPresenteNelDB()
            throws IOException, InterruptedException {
        account.setEmail("marlon.brando@alice.it");
        account.setPassword("1BcDEFG@");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC4 - EC40
    @Test
    public void loginEmailNonPresenteNelDBPasswordMaggioreUgualeDi8CaratteriConMaiuscoleMinuscoleCaratteriSpecialiENumeriEPresenteNelDB()
            throws IOException, InterruptedException {
        account.setEmail("marlon.brando@alice.it");
        account.setPassword("Pass.word1");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC5 - EC6
    @Test
    public void loginEmailPresenteNelDBPasswordVuota() throws IOException, InterruptedException {
        account.setEmail("aqewesa-8812@yopmail.com");
        account.setPassword("");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC5 - EC7
    @Test
    public void loginEmailPresenteNelDBPasswordNull() throws IOException, InterruptedException {
        account.setEmail("aqewesa-8812@yopmail.com");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC5 - EC8
    @Test
    public void loginEmailPresenteNelDBPasswordMinoreDi8CaratteriSenzaMaiuscoleSenzaMinuscoleSenzaCaratteriSpecialiSenzaNumeri()
            throws IOException, InterruptedException {
        account.setEmail("aqewesa-8812@yopmail.com");
        account.setPassword(" ");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC5 - EC9
    @Test
    public void loginEmailPresenteNelDBPasswordMinoreDi8CaratteriSoloNumeri() throws IOException, InterruptedException {
        account.setEmail("aqewesa-8812@yopmail.com");
        account.setPassword("1234567");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC5 - EC10
    @Test
    public void loginEmailPresenteNelDBPasswordMinoreDi8CaratteriSoloCaratteriSpeciali() throws IOException, InterruptedException {
        account.setEmail("aqewesa-8812@yopmail.com");
        account.setPassword("@.$%&.@");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC5 - EC11
    @Test
    public void loginEmailPresenteNelDBPasswordMinoreDi8CaratteriSoloCaratteriSpecialiENumeri() throws IOException, InterruptedException {
        account.setEmail("aqewesa-8812@yopmail.com");
        account.setPassword("@.$%&.7");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC5 - EC12
    @Test
    public void loginEmailPresenteNelDBPasswordMinoreDi8CaratteriSoloMinuscole() throws IOException, InterruptedException {
        account.setEmail("aqewesa-8812@yopmail.com");
        account.setPassword("abcdefg");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC5 - EC13
    @Test
    public void loginEmailPresenteNelDBPasswordMinoreDi8CaratteriSoloMinuscoleENumeri() throws IOException, InterruptedException {
        account.setEmail("aqewesa-8812@yopmail.com");
        account.setPassword("abcdef7");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC5 - EC14
    @Test
    public void loginEmailPresenteNelDBPasswordMinoreDi8CaratteriSoloMinuscoleECaratteriSpeciali() throws IOException, InterruptedException {
        account.setEmail("aqewesa-8812@yopmail.com");
        account.setPassword("abcdef@");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC5 - EC15
    @Test
    public void loginEmailPresenteNelDBPasswordMinoreDi8CaratteriSenzaMaiuscole() throws IOException, InterruptedException {
        account.setEmail("aqewesa-8812@yopmail.com");
        account.setPassword("ab3def@");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC5 - EC16
    @Test
    public void loginEmailPresenteNelDBPasswordMinoreDi8CaratteriSoloMaiuscole() throws IOException, InterruptedException {
        account.setEmail("aqewesa-8812@yopmail.com");
        account.setPassword("ABCDEFG");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC5 - EC17
    @Test
    public void loginEmailPresenteNelDBPasswordMinoreDi8CaratteriSoloMaiuscoleENumeri() throws IOException, InterruptedException {
        account.setEmail("aqewesa-8812@yopmail.com");
        account.setPassword("ABCDEF7");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC5 - EC18
    @Test
    public void loginEmailPresenteNelDBPasswordMinoreDi8CaratteriSoloMaiuscoleECaratteriSpeciali() throws IOException, InterruptedException {
        account.setEmail("aqewesa-8812@yopmail.com");
        account.setPassword("ABCDEF@");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC5 - EC19
    @Test
    public void loginEmailPresenteNelDBPasswordMinoreDi8CaratteriSenzaMinuscole() throws IOException, InterruptedException {
        account.setEmail("aqewesa-8812@yopmail.com");
        account.setPassword("AB3DEF@");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC5 - EC20
    @Test
    public void loginEmailPresenteNelDBPasswordMinoreDi8CaratteriSoloMaiuscoleEMinuscole() throws IOException, InterruptedException {
        account.setEmail("aqewesa-8812@yopmail.com");
        account.setPassword("ABCdefg");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC5 - EC21
    @Test
    public void loginEmailPresenteNelDBPasswordMinoreDi8CaratteriSenzaCaratteriSpeciali() throws IOException, InterruptedException {
        account.setEmail("aqewesa-8812@yopmail.com");
        account.setPassword("aB3DeF7");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC5 - EC22
    @Test
    public void loginEmailPresenteNelDBPasswordMinoreDi8CaratteriSenzaNumeri() throws IOException, InterruptedException {
        account.setEmail("aqewesa-8812@yopmail.com");
        account.setPassword("aB.DeFg");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC5 - EC23
    @Test
    public void loginEmailPresenteNelDBPasswordMinoreDi8Caratteri() throws IOException, InterruptedException {
        account.setEmail("aqewesa-8812@yopmail.com");
        account.setPassword("       ");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC5 - EC24
    @Test
    public void loginEmailPresenteNelDBPasswordMaggioreUgualeDi8Caratteri() throws IOException, InterruptedException {
        account.setEmail("aqewesa-8812@yopmail.com");
        account.setPassword("        ");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC5 - EC25
    @Test
    public void loginEmailPresenteNelDBPasswordMaggioreUgualeDi8CaratteriSoloNumeri() throws IOException, InterruptedException {
        account.setEmail("aqewesa-8812@yopmail.com");
        account.setPassword("12345678");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC5 - EC26
    @Test
    public void loginEmailPresenteNelDBPasswordMaggioreUgualeDi8CaratteriSoloCaratteriSpeciali() throws IOException, InterruptedException {
        account.setEmail("aqewesa-8812@yopmail.com");
        account.setPassword("@..@%&.@");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC5 - EC27
    @Test
    public void loginEmailPresenteNelDBPasswordMaggioreUgualeDi8CaratteriSoloCaratteriSpecialiENumeri() throws IOException, InterruptedException {
        account.setEmail("aqewesa-8812@yopmail.com");
        account.setPassword("@..@%&.8");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC5 - EC28
    @Test
    public void loginEmailPresenteNelDBPasswordMaggioreUgualeDi8CaratteriSoloMinuscole() throws IOException, InterruptedException {
        account.setEmail("aqewesa-8812@yopmail.com");
        account.setPassword("abcdefgh");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC5 - EC29
    @Test
    public void loginEmailPresenteNelDBPasswordMaggioreUgualeDi8CaratteriSoloMinuscoleENumeri() throws IOException, InterruptedException {
        account.setEmail("aqewesa-8812@yopmail.com");
        account.setPassword("abcdefg8");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC5 - EC30
    @Test
    public void loginEmailPresenteNelDBPasswordMaggioreUgualeDi8CaratteriSoloMinuscoleECaratteriSpeciali()
            throws IOException, InterruptedException {
        account.setEmail("aqewesa-8812@yopmail.com");
        account.setPassword("abcdefg@");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC5 - EC31
    @Test
    public void loginEmailPresenteNelDBPasswordMaggioreUgualeDi8CaratteriSenzaMaiuscole() throws IOException, InterruptedException {
        account.setEmail("aqewesa-8812@yopmail.com");
        account.setPassword("ab@defg8");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC5 - EC32
    @Test
    public void loginEmailPresenteNelDBPasswordMaggioreUgualeDi8CaratteriSoloMaiuscole() throws IOException, InterruptedException {
        account.setEmail("aqewesa-8812@yopmail.com");
        account.setPassword("ABCDEFGH");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC5 - EC33
    @Test
    public void loginEmailPresenteNelDBPasswordMaggioreUgualeDi8CaratteriSoloMaiuscoleENumeri() throws IOException, InterruptedException {
        account.setEmail("aqewesa-8812@yopmail.com");
        account.setPassword("ABCDEFG8");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC5 - EC34
    @Test
    public void loginEmailPresenteNelDBPasswordMaggioreUgualeDi8CaratteriSoloMaiuscoleECaratteriSpeciali()
            throws IOException, InterruptedException {
        account.setEmail("aqewesa-8812@yopmail.com");
        account.setPassword("ABCDEFG@");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC5 - EC35
    @Test
    public void loginEmailPresenteNelDBPasswordMaggioreUgualeDi8CaratteriSenzaMinuscole() throws IOException, InterruptedException {
        account.setEmail("aqewesa-8812@yopmail.com");
        account.setPassword("AB@DEFG8");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC5 - EC36
    @Test
    public void loginEmailPresenteNelDBPasswordMaggioreUgualeDi8CaratteriSoloMaiuscoleEMinuscole() throws IOException, InterruptedException {
        account.setEmail("aqewesa-8812@yopmail.com");
        account.setPassword("ABcDEFGh");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC5 - EC37
    @Test
    public void loginEmailPresenteNelDBPasswordMaggioreUgualeDi8CaratteriSenzaNumeri() throws IOException, InterruptedException {
        account.setEmail("aqewesa-8812@yopmail.com");
        account.setPassword("ABcDEFG@");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC5 - EC38
    @Test
    public void loginEmailPresenteNelDBPPasswordMaggioreUgualeDi8CaratteriConMaiuscoleMinuscoleCaratteriSpecialiENumeri()
            throws IOException, InterruptedException {
        account.setEmail("aqewesa-8812@yopmail.com");
        account.setPassword("1BcDEFG@");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC5 - EC39
    @Test
    public void loginEmailPresenteNelDBPPasswordMaggioreUgualeDi8CaratteriConMaiuscoleMinuscoleCaratteriSpecialiENumeriMaNonPresenteNelDB()
            throws IOException, InterruptedException {
        account.setEmail("aqewesa-8812@yopmail.com");
        account.setPassword("1BcDEFG@");
        assertFalse(accountDAO_cognito.login(account));
    }

    // Copre EC5 - EC40
    @Test
    public void loginEmailPresenteNelDBPPasswordMaggioreUgualeDi8CaratteriConMaiuscoleMinuscoleCaratteriSpecialiENumeriEPresenteNelDB()
            throws IOException, InterruptedException {
        account.setEmail("aqewesa-8812@yopmail.com");
        account.setPassword("Pass.word1");
        assertTrue(accountDAO_cognito.login(account));
    }


    // White-Box testing, metodologia Branch Coverage mediante GFC

    @Test
    public void loginPath_42_43() throws IOException, InterruptedException {
        account.setEmail(null);
        account.setPassword("somethingOk.12");
        assertFalse(accountDAO_cognito.login(account));
    }

    @Test
    public void loginPath_42_45_46() throws IOException, InterruptedException {
        account.setEmail("something@example.com");
        assertFalse(accountDAO_cognito.login(account));
    }

    @Test
    public void loginPath_42_45_48() throws IOException, InterruptedException {
        account.setEmail("something@example.com");
        account.setPassword("doesNotRespectPattern");
        assertFalse(accountDAO_cognito.login(account));
    }

    @Test
    public void loginPath_42_45_48_73() throws IOException, InterruptedException {
        account.setEmail("aqewesa-8812@yopmail.com");
        account.setPassword("Pass.word1");
        assertTrue(accountDAO_cognito.login(account));
    }

}
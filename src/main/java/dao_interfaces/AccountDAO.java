package dao_interfaces;

import models.Account;

import java.io.IOException;

/**
 * @author Alessandro Quirile, Mauro Telese
 */
public interface AccountDAO {
    boolean login(Account account) throws IOException, InterruptedException;
}

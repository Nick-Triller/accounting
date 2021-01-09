package core.reader;

import core.Ledger;

/**
 A Reader can read double-entries from a persistent store.
 */
public interface Reader {
    Ledger read();
}

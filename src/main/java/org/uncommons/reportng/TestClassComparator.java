package org.uncommons.reportng;

import java.util.Comparator;
import org.testng.IClass;

/**
 * Comparator for sorting classes alphabetically by fully-qualified name.
 */
class TestClassComparator implements Comparator<IClass> {

    public int compare(IClass class1, IClass class2) {
        return class1.getName().compareTo(class2.getName());
    }
}

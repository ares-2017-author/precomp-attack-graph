package computation;

import datatypes.OrdinalTtcValue;

public class OrdinalOperations {

    public static OrdinalTtcValue min(OrdinalTtcValue otv1, OrdinalTtcValue otv2) {

        if (otv1.equals(otv2)) {
            // identical values => identical result
            return otv1;
        } else if (otv1 == OrdinalTtcValue.ZERO || otv2 == OrdinalTtcValue.ZERO) {
            // whatever the other one is
            return OrdinalTtcValue.ZERO;
        } else if (otv1 == OrdinalTtcValue.INFINITE || otv1 == OrdinalTtcValue.UNDEFINED) {
            // whatever the compared one is except ZERO
            return otv2;
        } else if (otv2 == OrdinalTtcValue.INFINITE || otv2 == OrdinalTtcValue.UNDEFINED) {
            // whatever 'this' is except ZERO, INFINITE and UNDEFINED
            return otv1;
        } else if (otv1 == OrdinalTtcValue.LTESOURCE || otv2 == OrdinalTtcValue.LTESOURCE) {
            // LTESOURCE leads to LTESOURCE
            return OrdinalTtcValue.LTESOURCE;
        } else if ((otv1 == OrdinalTtcValue.SOURCE && otv2 == OrdinalTtcValue.ANY)
                || (otv1 == OrdinalTtcValue.ANY && otv2 == OrdinalTtcValue.SOURCE)) {
            // SOURCE and ANY leads to LTESOURCE
            return OrdinalTtcValue.LTESOURCE;
        } else if ((otv1 == OrdinalTtcValue.SOURCE && otv2 == OrdinalTtcValue.GTESOURCE)
                || (otv1 == OrdinalTtcValue.GTESOURCE && otv2 == OrdinalTtcValue.SOURCE)) {
            // SOURCE AND GTESOURCE leads to SOURCE
            return OrdinalTtcValue.SOURCE;
        } else {
            // GTESOURCE and ANY leads to ANY
            return OrdinalTtcValue.ANY;
        }
    }

    public static OrdinalTtcValue max(OrdinalTtcValue otv1, OrdinalTtcValue otv2) {
        if (otv1 == otv2) {
            return otv1;
        } else if (otv1 == OrdinalTtcValue.INFINITE || otv2 == OrdinalTtcValue.INFINITE) {
            // whatever the other one is
            return OrdinalTtcValue.INFINITE;
        } else if (otv1 == OrdinalTtcValue.ZERO || otv1 == OrdinalTtcValue.UNDEFINED) {
            // /whatever the other one is except INFINITE
            return otv2;
        } else if (otv2 == OrdinalTtcValue.ZERO || otv2 == OrdinalTtcValue.UNDEFINED) {
            //whatever the other one is except INFINITE, ZERO, and UNDEFINED
            return otv1;
        } else if (otv1 == OrdinalTtcValue.GTESOURCE || otv2 == OrdinalTtcValue.GTESOURCE) {
            //whatever the other one is except INFINITE, ZERO, and UNDEFINED
            return OrdinalTtcValue.GTESOURCE;
        } else if ((otv1 == OrdinalTtcValue.SOURCE && otv2 == OrdinalTtcValue.ANY)
                || (otv1 == OrdinalTtcValue.ANY && otv2 == OrdinalTtcValue.SOURCE)) {
            // ANY and SOURCE leads to GTESOURCE
            return OrdinalTtcValue.GTESOURCE;
        } else if ((otv1 == OrdinalTtcValue.SOURCE && otv2 == OrdinalTtcValue.LTESOURCE)
                || (otv1 == OrdinalTtcValue.LTESOURCE && otv2 == OrdinalTtcValue.SOURCE)) {
            // SOURCE and LTESOURCE leads to SOURCE
            return OrdinalTtcValue.SOURCE;
        } else {
            // ANY and LTESOURCE leads to ANY
            return OrdinalTtcValue.ANY;
        }

    }

    public static OrdinalTtcValue plus(OrdinalTtcValue otv1, OrdinalTtcValue otv2) {
        if (otv1 == OrdinalTtcValue.INFINITE || otv2 == OrdinalTtcValue.INFINITE) {
            //whatever the other one is
            return OrdinalTtcValue.INFINITE;
        } else if (otv1 == OrdinalTtcValue.ZERO) {
            //whatever the other one is
            return otv2;
        } else if (otv2 == OrdinalTtcValue.ZERO) {
            //whatever the other one is
            return otv1;
        } else if (otv1 == OrdinalTtcValue.GTESOURCE || otv2 == OrdinalTtcValue.GTESOURCE) {
            // And the other one is either LTESOURCE , ANY , SOURCE or GTESOURCE
            return OrdinalTtcValue.GTESOURCE;
        } else if (otv1 == OrdinalTtcValue.SOURCE || otv2 == OrdinalTtcValue.SOURCE) {
            // And the other one is either LTESOURCE, ANY, SOURCE
            return OrdinalTtcValue.GTESOURCE;
        } else {
            // if both are ANY
            return OrdinalTtcValue.ANY;
        }

    }
}

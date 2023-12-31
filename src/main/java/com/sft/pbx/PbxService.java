//package com.sft.pbx;
//
//import com.sft.user.LightUser;
//import com.sft.user.User;
//import com.sft.user.UserType;
//import com.sft.user.contact.PhoneNumber;
//
//import java.util.Optional;
//import java.util.function.Consumer;
//
//public class PbxService {
//
//    public void callNumber(User user, String number) {
//        callDstNumber(user, number);
//    }
//
//    public void callNumber(LightUser user, String number) {
//        callDstNumber(user, number);
//    }
//
//    private void callDstNumber(UserType user, String number) {
//        Consumer<PhoneNumber> call = callerId -> callNumber(callerId.number(), number);
//        Runnable anonymousCall = () -> callNumber("anonymous", number);
//
//        getCallerIdFromUser(user)
//                .ifPresentOrElse(call, anonymousCall);
//    }
//
//    private void callNumber(String callerId, String number) {
//        System.out.println("""
//                CallerId: %s
//                DstNumber: %s
//                """.formatted(callerId, number));
//    }
//
//    private Optional<PhoneNumber> getCallerIdFromUser(UserType userType) {
//        return userType.contacts().stream()
//                .filter(PhoneNumber.class::isInstance)
//                .map(PhoneNumber.class::cast)
//                .findFirst();
//    }
//}
//

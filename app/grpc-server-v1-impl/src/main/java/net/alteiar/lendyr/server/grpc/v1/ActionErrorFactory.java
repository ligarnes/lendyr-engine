package net.alteiar.lendyr.server.grpc.v1;

import net.alteiar.lendyr.entity.action.exception.ActionException;
import net.alteiar.lendyr.grpc.model.v1.encounter.LendyrAction;
import net.alteiar.lendyr.grpc.model.v1.game.LendyrActionResponse;
import net.alteiar.lendyr.grpc.model.v1.game.LendyrActionResultStatusType;

public interface ActionErrorFactory {

  static LendyrActionResponse notEnoughAction(ActionException ex) {
    return LendyrActionResponse.newBuilder()
        .setType(LendyrActionResultStatusType.NOT_ENOUGH_ACTION)
        .setErrorReason(ex.getMessage())
        .build();
  }

  static LendyrActionResponse notAllowed(ActionException ex) {
    return LendyrActionResponse.newBuilder()
        .setType(LendyrActionResultStatusType.NOT_ALLOWED)
        .setErrorReason(ex.getMessage())
        .build();
  }

  static LendyrActionResponse unexpectedError(LendyrAction request) {
    return LendyrActionResponse.newBuilder()
        .setType(LendyrActionResultStatusType.UNEXPECTED)
        .setErrorReason(String.format("Unexpected error while processing %s; please contact support", request.getActionsCase().name()))
        .build();
  }

  static LendyrActionResponse notImplemented(LendyrAction request) {
    return LendyrActionResponse.newBuilder()
        .setType(LendyrActionResultStatusType.NOT_IMPLEMENTED)
        .setErrorReason(request.getActionsCase().name() + " is not implemented yet")
        .build();
  }
}

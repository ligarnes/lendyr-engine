package net.alteiar.lendyr.server.grpc.v1;

import net.alteiar.lendyr.entity.action.exception.ActionException;
import net.alteiar.lendyr.grpc.model.v1.encounter.LendyrAction;
import net.alteiar.lendyr.grpc.model.v1.encounter.LendyrActionErrorResult;
import net.alteiar.lendyr.grpc.model.v1.encounter.LendyrActionResult;
import net.alteiar.lendyr.grpc.model.v1.encounter.LendyrActionResultStatusType;

public interface ActionErrorFactory {

  static LendyrActionResult notEnoughAction(ActionException ex) {
    return LendyrActionResult.newBuilder()
        .setType(LendyrActionResultStatusType.NOT_ENOUGH_ACTION)
        .setError(LendyrActionErrorResult.newBuilder().setDescription(ex.getMessage()).build())
        .build();
  }

  static LendyrActionResult notAllowed(ActionException ex) {
    return LendyrActionResult.newBuilder()
        .setType(LendyrActionResultStatusType.NOT_ALLOWED)
        .setError(LendyrActionErrorResult.newBuilder().setDescription(ex.getMessage()).build())
        .build();
  }

  static LendyrActionResult unexpectedError(LendyrAction request) {
    return LendyrActionResult.newBuilder()
        .setType(LendyrActionResultStatusType.UNEXPECTED)
        .setError(LendyrActionErrorResult.newBuilder()
            .setDescription(String.format("Unexpected error while processing %s; please contact support", request.getActionsCase().name())))
        .build();
  }

  static LendyrActionResult notImplemented(LendyrAction request) {
    return LendyrActionResult.newBuilder()
        .setType(LendyrActionResultStatusType.NOT_IMPLEMENTED)
        .setError(LendyrActionErrorResult.newBuilder().setDescription(request.getActionsCase().name() + " is not implemented yet"))
        .build();
  }
}

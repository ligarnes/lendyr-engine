package net.alteiar.lendyr.server.grpc.v1;

import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.Builder;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import net.alteiar.lendyr.engine.GameContext;
import net.alteiar.lendyr.entity.action.GameAction;
import net.alteiar.lendyr.entity.action.combat.EndTurnAction;
import net.alteiar.lendyr.entity.action.exception.NotAllowedException;
import net.alteiar.lendyr.entity.action.exception.NotEnoughActionException;
import net.alteiar.lendyr.entity.action.exception.NotSupportedException;
import net.alteiar.lendyr.entity.action.exception.ProcessingException;
import net.alteiar.lendyr.grpc.model.v1.encounter.LendyrAction;
import net.alteiar.lendyr.grpc.model.v1.encounter.LendyrGameEvent;
import net.alteiar.lendyr.grpc.model.v1.game.*;
import net.alteiar.lendyr.grpc.model.v1.item.LendyrItem;
import net.alteiar.lendyr.grpc.model.v1.map.LendyrMap;
import net.alteiar.lendyr.model.map.LayeredMap;
import net.alteiar.lendyr.model.map.MapFactory;
import net.alteiar.lendyr.persistence.dao.LocalMapDao;
import net.alteiar.lendyr.server.grpc.v1.mapper.ActionMapper;
import net.alteiar.lendyr.server.grpc.v1.mapper.GenericMapper;
import net.alteiar.lendyr.server.grpc.v1.mapper.ItemMapper;
import net.alteiar.lendyr.server.grpc.v1.mapper.WorldMapMapper;
import net.alteiar.lendyr.server.grpc.v1.processor.CurrentStateProcessor;

import java.util.List;
import java.util.UUID;


@Log4j2
public class LendyrGameServiceImpl extends LendyrGameServiceGrpc.LendyrGameServiceImplBase {
  private final GameContext gameContext;
  private final CurrentStateProcessor currentStateProcessor;

  @Builder
  LendyrGameServiceImpl(@NonNull GameContext gameContext) {
    this.gameContext = gameContext;
    currentStateProcessor = CurrentStateProcessor.builder().gameContext(gameContext).build();
    this.gameContext.setListener(currentStateProcessor);
  }

  @Override
  public void load(LendyrLoadGameRequest request, StreamObserver<EmptyResponse> responseObserver) {
    Timer.time("load",
        () -> {
          this.gameContext.load(request.getSaveName());
          responseObserver.onNext(EmptyResponse.getDefaultInstance());
          responseObserver.onCompleted();
        }
    );
  }

  @Override
  public void save(LendyrSaveGameRequest request, StreamObserver<EmptyResponse> responseObserver) {
    Timer.time("load",
        () -> {
          this.gameContext.save(request.getSaveName());
          responseObserver.onNext(EmptyResponse.getDefaultInstance());
          responseObserver.onCompleted();
        }
    );
  }

  @Override
  public void pause(EmptyResponse request, StreamObserver<EmptyResponse> responseObserver) {
    Timer.time("pause",
        () -> {
          this.gameContext.pause();
          responseObserver.onNext(EmptyResponse.getDefaultInstance());
          responseObserver.onCompleted();
        }
    );
  }

  @Override
  public void resume(EmptyResponse request, StreamObserver<EmptyResponse> responseObserver) {
    Timer.time("resume",
        () -> {
          this.gameContext.resume();
          responseObserver.onNext(EmptyResponse.getDefaultInstance());
          responseObserver.onCompleted();
        }
    );
  }

  @Override
  public void registerActions(EmptyResponse request, StreamObserver<LendyrGameEvent> responseObserver) {
    log.info("Request actions");
    try {
      boolean isCompleted = false;
      while (!isCompleted) {
        try {
          //log.info("Publish new actions");
          currentStateProcessor.awaitNewAction(1000).ifPresent(responseObserver::onNext);
        } catch (InterruptedException e) {
          log.warn("Interrupted while waiting for current state", e);
        }
      }
    } catch (RuntimeException e) {
      log.warn(e);
      Status status = Status.INTERNAL.withDescription(e.getMessage());
      responseObserver.onError(status.asRuntimeException());
      return;
    }
    responseObserver.onCompleted();
  }

  @Override
  public void currentState(EmptyResponse request, StreamObserver<LendyrGameState> responseObserver) {
    Timer.time("currentState",
        () -> {
          responseObserver.onNext(currentStateProcessor.currentGameState());
          responseObserver.onCompleted();
        }
    );
  }

  @Override
  public void act(LendyrAction request, StreamObserver<LendyrActionResponse> responseObserver) {
    Timer.time("Act " + request.getActionsCase().name(),
        () -> {
          try {
            GameAction action = ActionMapper.INSTANCE.gameActionToBusiness(request);
            gameContext.act(action);
            responseObserver.onNext(LendyrActionResponse.newBuilder().setType(LendyrActionResultStatusType.SUCCESS).build());
          } catch (NotSupportedException e) {
            log.warn("Action {} not supported", request.getActionsCase().name(), e);
            responseObserver.onNext(ActionErrorFactory.notImplemented(request));
          } catch (ProcessingException e) {
            log.warn("Action {} processing failed", request.getActionsCase().name(), e);
            responseObserver.onNext(ActionErrorFactory.unexpectedError(request));
          } catch (NotEnoughActionException e) {
            log.warn("Not enough action for {}", request.getActionsCase().name(), e);
            responseObserver.onNext(ActionErrorFactory.notEnoughAction(e));
          } catch (NotAllowedException e) {
            log.warn("Action {} not allowed", request.getActionsCase().name(), e);
            responseObserver.onNext(ActionErrorFactory.notAllowed(e));
          } catch (RuntimeException ex) {
            log.warn("Action {}, unexpected exception", request.getActionsCase().name(), ex);
            responseObserver.onError(ex);
            return;
          }
          responseObserver.onCompleted();
        }
    );
  }

  @Override
  public void endTurn(EmptyResponse request, StreamObserver<EmptyResponse> responseObserver) {
    Timer.time("End Turn",
        () -> {
          gameContext.act(new EndTurnAction());
          responseObserver.onNext(EmptyResponse.newBuilder().build());
          responseObserver.onCompleted();
        }
    );
  }

  @Override
  public void getItems(EmptyResponse request, StreamObserver<LendyrItems> responseObserver) {
    Timer.time("getItems",
        () -> {
          List<LendyrItem> items = gameContext.getGame().getItemRepository().findAll().stream().map(ItemMapper.INSTANCE::itemToDto).toList();
          responseObserver.onNext(LendyrItems.newBuilder().addAllItems(items).build());
          responseObserver.onCompleted();
        }
    );
  }

  @Override
  public void getMaps(LendyrGetById request, StreamObserver<LendyrMap> responseObserver) {
    Timer.time("getMaps",
        () -> {
          UUID id = GenericMapper.INSTANCE.convertBytesToUUID(request.getId());
          LocalMapDao dao = gameContext.getGame().getMapRepository().findMapById(id);
          MapFactory mapFactory = new MapFactory(dao.getTiledMap());
          LayeredMap layeredMap = mapFactory.load();

          responseObserver.onNext(WorldMapMapper.INSTANCE.mapToDto(dao.getMap(), layeredMap));
          responseObserver.onCompleted();
        }
    );
  }
}



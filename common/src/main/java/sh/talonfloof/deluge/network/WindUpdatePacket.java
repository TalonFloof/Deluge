package sh.talonfloof.deluge.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import sh.talonfloof.deluge.Deluge;
import sh.talonfloof.deluge.DelugeEventType;

public record WindUpdatePacket(float angle, float speed) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<WindUpdatePacket> TYPE = new CustomPacketPayload.Type<>(Deluge.path("wind_update"));

    public static final StreamCodec<ByteBuf, WindUpdatePacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.FLOAT,
            WindUpdatePacket::angle,
            ByteBufCodecs.FLOAT,
            WindUpdatePacket::speed,
            WindUpdatePacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void send(ServerPlayer player, float angle, float speed) {
        if(Deluge.NETWORK.canSend(TYPE.id(), player)) {
            Deluge.NETWORK.send(new WindUpdatePacket(angle,speed), player);
        }
    }
}

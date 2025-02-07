package org.firstinspires.ftc.teamcode.teleOp;

import android.annotation.SuppressLint;

import com.arcrobotics.ftclib.command.CommandOpMode;
import com.arcrobotics.ftclib.command.PerpetualCommand;
import com.arcrobotics.ftclib.command.button.Trigger;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.auto.BulkCacheCommand;
import org.firstinspires.ftc.teamcode.commands.armcommands.ManualArmCommand;
import org.firstinspires.ftc.teamcode.commands.armcommands.ProfiledArmCommand;
import org.firstinspires.ftc.teamcode.commands.presets.ArmToScoringCommand;
import org.firstinspires.ftc.teamcode.subsystems.ArmCorrected;
import org.firstinspires.ftc.teamcode.subsystems.Claw;
import org.firstinspires.ftc.teamcode.subsystems.Diffy;

@TeleOp
public class ProfiledArmTester extends CommandOpMode {
    private ArmCorrected arm;
    private Claw claw;
    private Diffy diffy;
    ElapsedTime timer;
    private ManualArmCommand manualArmCommand;

    @Override
    public void initialize() {
        schedule(new BulkCacheCommand(hardwareMap));
        GamepadEx manipulator = new GamepadEx(gamepad2);

        arm = new ArmCorrected(hardwareMap);
        claw = new Claw(hardwareMap);
        timer = new ElapsedTime();

        timer.reset();

        manualArmCommand = new ManualArmCommand(arm, manipulator);

        arm.setDefaultCommand(new PerpetualCommand(manualArmCommand));

        new Trigger(() -> manipulator.getButton(GamepadKeys.Button.DPAD_DOWN))
                .whenActive(new ArmToScoringCommand(arm, claw, diffy, ArmToScoringCommand.Presets.BASKET_HIGH)
                        .withTimeout(2500)
                        .interruptOn(() -> manualArmCommand.isManualActive()));

        new Trigger(() -> manipulator.getButton(GamepadKeys.Button.DPAD_UP))
                .whenActive(new ArmToScoringCommand(arm, claw, diffy, ArmToScoringCommand.Presets.GRAB_SUB)
                        .withTimeout(2500)
                        .interruptOn(() -> manualArmCommand.isManualActive()));

        new Trigger(() -> manipulator.getButton(GamepadKeys.Button.DPAD_LEFT))
                .whenActive(new ArmToScoringCommand(arm, claw, diffy, ArmToScoringCommand.Presets.HOVER_SUB)
                        .withTimeout(2500)
                        .interruptOn(() -> manualArmCommand.isManualActive()));

        new Trigger(() -> manipulator.getButton(GamepadKeys.Button.DPAD_RIGHT))
                .whenActive(new ArmToScoringCommand(arm, claw, diffy, ArmToScoringCommand.Presets.REST)
                        .withTimeout(2500)
                        .interruptOn(() -> manualArmCommand.isManualActive()));

        telemetry.addLine("Initialization Done");
        telemetry.update();
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void run() {
        super.run();

//        telemetry.addData("Gravity Coefficent >", );
        telemetry.addData("position >", arm.getCurrentPosition());
        telemetry.update();
    }
}

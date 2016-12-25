package sample;

import javafx.animation.*;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.BoxBlur;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;


import static java.lang.Math.random;

public class Main extends Application {

    private static long lastTimeStamp = 0;
    private static double mouseXCoordinate = 0;
    private static double mouseYCoordinate = 0;


    @Override
    public void start(Stage primaryStage) throws Exception{

        // the size of the Group node is dictated by the size of the nodes within it
        Group root = new Group();
        Scene scene = new Scene(root, 1900, 1000, Color.LIGHTSEAGREEN);

        Group circles = new Group();
        for (int i = 0; i < 60; i++) {
            Circle circle = new Circle(75, Color.web("white", 0.05));
            circle.setStrokeType(StrokeType.OUTSIDE);
            circle.setStroke(Color.web("white", 0.16));
            circle.setStrokeWidth(4);
            circle.setCenterX(scene.getWidth() / 2);
            circle.setCenterY(scene.getHeight() / 2);
            circles.getChildren().add(circle);
        }

        Rectangle colors = new Rectangle(scene.getWidth(), scene.getHeight(),
                new LinearGradient(0f, 1f, 1f, 0f, true, CycleMethod.NO_CYCLE, new
                    Stop[] {
                        new Stop(0, Color.web("#f8bd55")),
                        new Stop(0.14, Color.web("#c0fe56")),
                        new Stop(0.28, Color.web("#5dfbc1")),
                        new Stop(0.43, Color.web("#64c2f8")),
                        new Stop(0.57, Color.web("#be4af7")),
                        new Stop(0.71, Color.web("#ed5fc2")),
                        new Stop(0.85, Color.web("#ef504c")),
                        new Stop(1, Color.web("#f2660f")),
                    }));
        colors.widthProperty().bind(scene.widthProperty());
        colors.heightProperty().bind(scene.heightProperty());

        //root.getChildren().add(colors);
        //root.getChildren().add(circles);
        Group blendModeGroup = new Group(new Group(new Rectangle(scene.getWidth(), scene.getHeight(),
                Color.BLACK), circles), colors);
        colors.setBlendMode(BlendMode.OVERLAY);
        root.getChildren().add(blendModeGroup);

        circles.setEffect(new BoxBlur(10, 10, 3));

        primaryStage.setScene(scene);

        scene.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                mouseXCoordinate = event.getSceneX();
                mouseYCoordinate = event.getSceneY();

                // Node thisRandomCircle = circles.getChildren().get(0);
                Node thisRandomCircle = circles.getChildren().get((int) (circles.getChildren().size() * random()));
                System.out.printf("%f %f ----", mouseXCoordinate, mouseYCoordinate);

                Timeline timeline = new Timeline();
                timeline.getKeyFrames().addAll(
                        new KeyFrame(Duration.ZERO,
                                // start is where the circle is now
                                new KeyValue(thisRandomCircle.layoutXProperty(), thisRandomCircle.getLayoutX()),
                                new KeyValue(thisRandomCircle.layoutYProperty(), thisRandomCircle.getLayoutY())),
                        new KeyFrame(new Duration(600),
                                // end is where the mouse is
                                new KeyValue(thisRandomCircle.layoutXProperty(),
                                        (mouseXCoordinate - thisRandomCircle.getLayoutBounds().getMinX() - (thisRandomCircle.getLayoutBounds().getWidth() / 2) )),
                                new KeyValue(thisRandomCircle.layoutYProperty(),
                                        (mouseYCoordinate - thisRandomCircle.getLayoutBounds().getMinY() - (thisRandomCircle.getLayoutBounds().getHeight() / 2) ),
                                        Interpolator.EASE_OUT))
                );
                timeline.play();
                mouseXCoordinate = 0;
                mouseYCoordinate = 0;
            }
        });

        // add randomness
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if ((now - lastTimeStamp) > ((Math.pow(10, 9) / 3 ))) {
                    lastTimeStamp = now;
                    System.out.println("We are in the timer");

                    Node thisRandomCircle = circles.getChildren().get((int) (circles.getChildren().size() * random()));
                    double randomXCoordinate = random() * scene.getWidth();
                    double randomYCoordinate = random() * scene.getHeight();
                    // thisRandomCircle.setVisible(false); // to test if we are doing anything

                    Timeline timeline = new Timeline();
                    timeline.getKeyFrames().addAll(
                            new KeyFrame(Duration.ZERO,
                                    // start is where the circle is now
                                    new KeyValue(thisRandomCircle.layoutXProperty(), thisRandomCircle.getLayoutX()),
                                    new KeyValue(thisRandomCircle.layoutYProperty(), thisRandomCircle.getLayoutY())),
                            new KeyFrame(new Duration(600), // should change 600 to dynamic value based on distance to move!
                                    // end is ... random
                                    new KeyValue(thisRandomCircle.layoutXProperty(),
                                            (randomXCoordinate - thisRandomCircle.getLayoutBounds().getMinX() - (thisRandomCircle.getLayoutBounds().getWidth() / 2) )),
                                    new KeyValue(thisRandomCircle.layoutYProperty(),
                                            (randomYCoordinate - thisRandomCircle.getLayoutBounds().getMinY() - (thisRandomCircle.getLayoutBounds().getHeight() / 2) ),
                                            Interpolator.EASE_OUT))
                    );
                    timeline.play();
                    System.out.println("sort of done with the timer timeline?!?");
                }
            }
        };
        timer.start();


        primaryStage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}

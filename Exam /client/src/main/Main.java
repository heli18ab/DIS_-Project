package main;

import controller.AccountController;
import controller.MainController;
import model.Account;
import util.NetworkUtil;
import view.MainView;

public class Main {

    public static void main(String[] args) {


        NetworkUtil util = new NetworkUtil();

        MainController controller = new MainController((util));

        MainView view = new MainView(controller);
        controller.setMainView(view);

        view.loginMainView().get();
    }

    }

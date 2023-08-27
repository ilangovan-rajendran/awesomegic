package com.awesomegic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Scanner;

@SpringBootApplication(scanBasePackages = {"com.awesomegic"},exclude = {DataSourceAutoConfiguration.class })
public class AwesomegicApplication implements CommandLineRunner{

	@Autowired
	private AwesomeGicBankingAction awesomeGicBankingAction;

	public static void main(String[] args) {
		SpringApplication.run(AwesomegicApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("Welcome to AwesomeGIC Bank! What would you like to do?\n" +
				"[I]nput transactions \n" +
				"[D]efine interest rules\n" +
				"[P]rint statement\n" +
				"[Q]uit");
		Scanner scanner = new Scanner(System.in);
		while(scanner.hasNextLine()) {
			String line = scanner.nextLine();
			if(line.equals("Q")) {
				break;
			}
			awesomeGicBankingAction.processAction(line);
		}
		scanner.close();
		System.exit(0);
	}
}

Socket.io server library from https://github.com/mrniko/netty-socketio
```
<plugin>
 <artifactId>maven-assembly-plugin</artifactId>
 <version>2.5.3</version>
 <configuration>
   <descriptorRefs>
     <descriptorRef>jar-with-dependencies</descriptorRef>
   </descriptorRefs>
 </configuration>
 <executions>
   <execution>
     <id>make-assembly</id> <!-- this is used for inheritance merges -->
     <phase>package</phase> <!-- bind to the packaging phase -->
     <goals>
       <goal>single</goal>
     </goals>
   </execution>
 </executions>
</plugin>
</plugins>
``` added for packaging to the end of the last <plugins>
